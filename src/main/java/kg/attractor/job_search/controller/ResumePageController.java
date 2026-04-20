package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.repository.ContactInfoRepository;
import kg.attractor.job_search.repository.ContactTypeRepository;
import kg.attractor.job_search.repository.EducationInfoRepository;
import kg.attractor.job_search.repository.WorkExperienceInfoRepository;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ResumePageController {

    private final ResumeService resumeService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ContactTypeRepository contactTypeRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final EducationInfoRepository educationInfoRepository;
    private final WorkExperienceInfoRepository workExperienceInfoRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private User getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private Map<String, String> buildResumeUpdateTimeMap(List<Resume> resumes) {
        Map<String, String> formattedDates = new LinkedHashMap<>();

        for (Resume resume : resumes) {
            formattedDates.put(resume.getId().toString(), formatDateTime(resume.getUpdateTime()));
        }

        return formattedDates;
    }

    @GetMapping("/resumes")
    public String resumesPage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can view their resumes");
        }

        List<Resume> resumes = resumeService.getByApplicantId(currentUser.getId());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resumes", resumes);
        model.addAttribute("resumeUpdateTimes", buildResumeUpdateTimeMap(resumes));

        return "resume-list";
    }

    @GetMapping("/resumes/create")
    public String createResumePage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can create resumes");
        }

        model.addAttribute("resume", new CreateResumeDto());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("contactTypes", contactTypeRepository.findAll());
        model.addAttribute("currentUser", currentUser);

        return "resume-form";
    }

    @PostMapping("/resumes/create")
    public String createResume(
            @Valid @ModelAttribute("resume") CreateResumeDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can create resumes");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("contactTypes", contactTypeRepository.findAll());
            model.addAttribute("currentUser", currentUser);
            return "resume-form";
        }

        dto.setIsActive(true);
        resumeService.create(dto, currentUser.getId());
        return "redirect:/resumes";
    }

    @GetMapping("/resumes/edit/{id}")
    public String editResumePage(@PathVariable Integer id, Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can edit resumes");
        }

        Resume existingResume = resumeService.getById(id)
                .orElseThrow(() -> new NotFoundException("Resume not found with id = " + id));

        if (!existingResume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can edit only your own resume");
        }

        ContactInfo contactInfo = contactInfoRepository.findByResumeId(id).stream().findFirst().orElse(null);
        EducationInfo educationInfo = educationInfoRepository.findByResumeId(id).stream().findFirst().orElse(null);
        WorkExperienceInfo workExperienceInfo = workExperienceInfoRepository.findByResumeId(id).stream().findFirst().orElse(null);

        UpdateResumeDto dto = UpdateResumeDto.builder()
                .name(existingResume.getName())
                .categoryId(existingResume.getCategoryId())
                .salary(existingResume.getSalary())
                .isActive(existingResume.getIsActive())
                .contactTypeId(contactInfo != null && contactInfo.getType() != null ? contactInfo.getType().getId() : null)
                .contactValue(contactInfo != null ? contactInfo.getContactValue() : null)
                .institution(educationInfo != null ? educationInfo.getInstitution() : null)
                .program(educationInfo != null ? educationInfo.getProgram() : null)
                .startDate(educationInfo != null && educationInfo.getStartDate() != null ? educationInfo.getStartDate().toString() : null)
                .endDate(educationInfo != null && educationInfo.getEndDate() != null ? educationInfo.getEndDate().toString() : null)
                .degree(educationInfo != null ? educationInfo.getDegree() : null)
                .years(workExperienceInfo != null ? workExperienceInfo.getYears() : null)
                .companyName(workExperienceInfo != null ? workExperienceInfo.getCompanyName() : null)
                .position(workExperienceInfo != null ? workExperienceInfo.getPosition() : null)
                .responsibilities(workExperienceInfo != null ? workExperienceInfo.getResponsibilities() : null)
                .build();

        model.addAttribute("resume", dto);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("contactTypes", contactTypeRepository.findAll());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resumeId", id);

        return "resume-form";
    }

    @PostMapping("/resumes/edit/{id}")
    public String editResume(
            @PathVariable Integer id,
            @Valid @ModelAttribute("resume") UpdateResumeDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can edit resumes");
        }

        Resume existingResume = resumeService.getById(id)
                .orElseThrow(() -> new NotFoundException("Resume not found with id = " + id));

        if (!existingResume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can edit only your own resume");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("contactTypes", contactTypeRepository.findAll());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resumeId", id);
            return "resume-form";
        }

        resumeService.update(id, dto);
        return "redirect:/resumes";
    }

    @GetMapping("/employer/resumes")
    public String employerResumesPage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can view all resumes");
        }

        List<Resume> resumes = resumeService.getAll();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resumes", resumes);
        model.addAttribute("resumeUpdateTimes", buildResumeUpdateTimeMap(resumes));

        return "employer-resume-list";
    }
}