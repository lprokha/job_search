package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.exception.VacancyNotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.repository.ContactInfoRepository;
import kg.attractor.job_search.repository.ContactTypeRepository;
import kg.attractor.job_search.repository.EducationInfoRepository;
import kg.attractor.job_search.repository.WorkExperienceInfoRepository;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.RespondedApplicantService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ResumePageController {

    private final ResumeService resumeService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final VacancyService vacancyService;
    private final RespondedApplicantService respondedApplicantService;
    private final ContactTypeRepository contactTypeRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final EducationInfoRepository educationInfoRepository;
    private final WorkExperienceInfoRepository workExperienceInfoRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private User getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
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

    private void addResumeDetailsToModel(Integer id, User currentUser, Model model) {
        Resume resume = resumeService.getById(id)
                .orElseThrow(ResumeNotFoundException::new);

        List<ContactInfo> contactInfos = contactInfoRepository.findByResumeId(id);
        List<EducationInfo> educationInfos = educationInfoRepository.findByResumeId(id);
        List<WorkExperienceInfo> workExperienceInfos = workExperienceInfoRepository.findByResumeId(id);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resume", resume);
        model.addAttribute("contactInfos", contactInfos);
        model.addAttribute("educationInfos", educationInfos);
        model.addAttribute("workExperienceInfos", workExperienceInfos);
        model.addAttribute("updatedAt", formatDateTime(resume.getUpdateTime()));

        if (currentUser.getAccountType() == AccountType.EMPLOYER) {
            List<Vacancy> employerVacancies = vacancyService.getByAuthorId(currentUser.getId()).stream()
                    .filter(vacancy -> Boolean.TRUE.equals(vacancy.getIsActive()))
                    .toList();

            model.addAttribute("employerVacancies", employerVacancies);
        }
    }

    private void fillResumeFormModel(User currentUser, Model model, Integer resumeId) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("contactTypes", contactTypeRepository.findAll());
        model.addAttribute("currentUser", currentUser);

        if (resumeId != null) {
            model.addAttribute("resumeId", resumeId);
        }
    }

    private void validateCreateResume(CreateResumeDto dto, BindingResult bindingResult) {
        validateCreateContactInfos(dto.getContactInfos(), bindingResult);
        validateCreateEducationInfos(dto.getEducationInfos(), bindingResult);
        validateCreateWorkExperienceInfos(dto.getWorkExperienceInfos(), bindingResult);
    }

    private void validateUpdateResume(UpdateResumeDto dto, BindingResult bindingResult) {
        validateUpdateContactInfos(dto.getContactInfos(), bindingResult);
        validateUpdateEducationInfos(dto.getEducationInfos(), bindingResult);
        validateUpdateWorkExperienceInfos(dto.getWorkExperienceInfos(), bindingResult);
    }

    private void validateCreateContactInfos(List<CreateResumeDto.ContactDto> contactInfos,
                                            BindingResult bindingResult) {
        if (contactInfos == null) {
            return;
        }

        for (int i = 0; i < contactInfos.size(); i++) {
            CreateResumeDto.ContactDto contact = contactInfos.get(i);
            validateContact(
                    contact.getTypeId(),
                    contact.getContactValue(),
                    "contactInfos[" + i + "].typeId",
                    "contactInfos[" + i + "].contactValue",
                    bindingResult
            );
        }
    }

    private void validateUpdateContactInfos(List<UpdateResumeDto.ContactDto> contactInfos,
                                            BindingResult bindingResult) {
        if (contactInfos == null) {
            return;
        }

        for (int i = 0; i < contactInfos.size(); i++) {
            UpdateResumeDto.ContactDto contact = contactInfos.get(i);
            validateContact(
                    contact.getTypeId(),
                    contact.getContactValue(),
                    "contactInfos[" + i + "].typeId",
                    "contactInfos[" + i + "].contactValue",
                    bindingResult
            );
        }
    }

    private void validateContact(Integer typeId,
                                 String contactValue,
                                 String typeField,
                                 String valueField,
                                 BindingResult bindingResult) {
        boolean typeEmpty = typeId == null;
        boolean valueEmpty = isBlank(contactValue);

        if (typeEmpty && valueEmpty) {
            return;
        }

        if (typeEmpty) {
            bindingResult.rejectValue(typeField, "validation.contact.type.required");
        }

        if (valueEmpty) {
            bindingResult.rejectValue(valueField, "validation.contact.value.required");
        }
    }

    private void validateCreateEducationInfos(List<CreateResumeDto.EducationDto> educationInfos,
                                              BindingResult bindingResult) {
        if (educationInfos == null) {
            return;
        }

        for (int i = 0; i < educationInfos.size(); i++) {
            CreateResumeDto.EducationDto education = educationInfos.get(i);

            validateEducation(
                    education.getInstitution(),
                    education.getProgram(),
                    education.getStartDate(),
                    education.getEndDate(),
                    education.getDegree(),
                    "educationInfos[" + i + "].institution",
                    "educationInfos[" + i + "].program",
                    "educationInfos[" + i + "].startDate",
                    "educationInfos[" + i + "].endDate",
                    "educationInfos[" + i + "].degree",
                    bindingResult
            );
        }
    }

    private void validateUpdateEducationInfos(List<UpdateResumeDto.EducationDto> educationInfos,
                                              BindingResult bindingResult) {
        if (educationInfos == null) {
            return;
        }

        for (int i = 0; i < educationInfos.size(); i++) {
            UpdateResumeDto.EducationDto education = educationInfos.get(i);

            validateEducation(
                    education.getInstitution(),
                    education.getProgram(),
                    education.getStartDate(),
                    education.getEndDate(),
                    education.getDegree(),
                    "educationInfos[" + i + "].institution",
                    "educationInfos[" + i + "].program",
                    "educationInfos[" + i + "].startDate",
                    "educationInfos[" + i + "].endDate",
                    "educationInfos[" + i + "].degree",
                    bindingResult
            );
        }
    }

    private void validateEducation(String institution,
                                   String program,
                                   String startDateValue,
                                   String endDateValue,
                                   String degree,
                                   String institutionField,
                                   String programField,
                                   String startDateField,
                                   String endDateField,
                                   String degreeField,
                                   BindingResult bindingResult) {
        if (isEmptyEducation(institution, program, startDateValue, endDateValue, degree)) {
            return;
        }

        if (isBlank(institution)) {
            bindingResult.rejectValue(institutionField, "validation.education.institution.required");
        }

        if (isBlank(program)) {
            bindingResult.rejectValue(programField, "validation.education.program.required");
        }

        if (isBlank(startDateValue)) {
            bindingResult.rejectValue(startDateField, "validation.education.startDate.required");
        }

        if (isBlank(endDateValue)) {
            bindingResult.rejectValue(endDateField, "validation.education.endDate.required");
        }

        if (isBlank(degree)) {
            bindingResult.rejectValue(degreeField, "validation.education.degree.required");
        }

        LocalDate startDate = parseDateForValidation(startDateValue, startDateField, bindingResult);
        LocalDate endDate = parseDateForValidation(endDateValue, endDateField, bindingResult);

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            bindingResult.rejectValue(endDateField, "validation.resume.educationPeriod");
        }
    }

    private void validateCreateWorkExperienceInfos(List<CreateResumeDto.WorkExperienceDto> workExperienceInfos,
                                                   BindingResult bindingResult) {
        if (workExperienceInfos == null) {
            return;
        }

        for (int i = 0; i < workExperienceInfos.size(); i++) {
            CreateResumeDto.WorkExperienceDto work = workExperienceInfos.get(i);

            validateWorkExperience(
                    work.getYears(),
                    work.getCompanyName(),
                    work.getPosition(),
                    work.getResponsibilities(),
                    "workExperienceInfos[" + i + "].years",
                    "workExperienceInfos[" + i + "].companyName",
                    "workExperienceInfos[" + i + "].position",
                    "workExperienceInfos[" + i + "].responsibilities",
                    bindingResult
            );
        }
    }

    private void validateUpdateWorkExperienceInfos(List<UpdateResumeDto.WorkExperienceDto> workExperienceInfos,
                                                   BindingResult bindingResult) {
        if (workExperienceInfos == null) {
            return;
        }

        for (int i = 0; i < workExperienceInfos.size(); i++) {
            UpdateResumeDto.WorkExperienceDto work = workExperienceInfos.get(i);

            validateWorkExperience(
                    work.getYears(),
                    work.getCompanyName(),
                    work.getPosition(),
                    work.getResponsibilities(),
                    "workExperienceInfos[" + i + "].years",
                    "workExperienceInfos[" + i + "].companyName",
                    "workExperienceInfos[" + i + "].position",
                    "workExperienceInfos[" + i + "].responsibilities",
                    bindingResult
            );
        }
    }

    private void validateWorkExperience(Integer years,
                                        String companyName,
                                        String position,
                                        String responsibilities,
                                        String yearsField,
                                        String companyField,
                                        String positionField,
                                        String responsibilitiesField,
                                        BindingResult bindingResult) {
        if (isEmptyWorkExperience(years, companyName, position, responsibilities)) {
            return;
        }

        if (years == null) {
            bindingResult.rejectValue(yearsField, "validation.work.years.required");
        }

        if (isBlank(companyName)) {
            bindingResult.rejectValue(companyField, "validation.work.company.required");
        }

        if (isBlank(position)) {
            bindingResult.rejectValue(positionField, "validation.work.position.required");
        }

        if (isBlank(responsibilities)) {
            bindingResult.rejectValue(responsibilitiesField, "validation.work.responsibilities.required");
        }
    }

    private boolean isEmptyEducation(String institution,
                                     String program,
                                     String startDate,
                                     String endDate,
                                     String degree) {
        return isBlank(institution)
                && isBlank(program)
                && isBlank(startDate)
                && isBlank(endDate)
                && isBlank(degree);
    }

    private boolean isEmptyWorkExperience(Integer years,
                                          String companyName,
                                          String position,
                                          String responsibilities) {
        return years == null
                && isBlank(companyName)
                && isBlank(position)
                && isBlank(responsibilities);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private LocalDate parseDateForValidation(String value,
                                             String fieldName,
                                             BindingResult bindingResult) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            bindingResult.rejectValue(fieldName, "validation.resume.date.invalid");
            return null;
        }
    }

    @GetMapping("/resumes")
    public String resumesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Authentication authentication,
            Model model
    ) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can view their resumes");
        }

        Page<Resume> resumePage = resumeService.getByApplicantId(currentUser.getId(), page, size);
        List<Resume> resumes = resumePage.getContent();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resumes", resumes);
        model.addAttribute("resumeUpdateTimes", buildResumeUpdateTimeMap(resumes));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resumePage.getTotalPages());

        return "resume-list";
    }

    @GetMapping("/resumes/{id}")
    public String applicantResumeDetailPage(@PathVariable Integer id,
                                            Authentication authentication,
                                            Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can view this resume page");
        }

        Resume resume = resumeService.getById(id)
                .orElseThrow(ResumeNotFoundException::new);

        if (!resume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can view only your own resume");
        }

        addResumeDetailsToModel(id, currentUser, model);

        return "resume-detail";
    }

    @GetMapping("/employer/resumes/{id}")
    public String employerResumeDetailPage(@PathVariable Integer id,
                                           Authentication authentication,
                                           Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can view applicant resumes");
        }

        Resume resume = resumeService.getById(id)
                .orElseThrow(ResumeNotFoundException::new);

        if (!Boolean.TRUE.equals(resume.getIsActive())) {
            throw new ResumeNotFoundException();
        }

        addResumeDetailsToModel(id, currentUser, model);

        return "resume-detail";
    }

    @PostMapping("/employer/resumes/{id}/offer-vacancy")
    public String offerVacancyToApplicant(@PathVariable Integer id,
                                          @RequestParam Integer vacancyId,
                                          Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can offer vacancies");
        }

        Resume resume = resumeService.getById(id)
                .orElseThrow(ResumeNotFoundException::new);

        if (!Boolean.TRUE.equals(resume.getIsActive())) {
            throw new ResumeNotFoundException();
        }

        Vacancy vacancy = vacancyService.getById(vacancyId)
                .orElseThrow(VacancyNotFoundException::new);

        if (!vacancy.getAuthorId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can offer only your own vacancy");
        }

        if (!Boolean.TRUE.equals(vacancy.getIsActive())) {
            throw new VacancyNotFoundException();
        }

        RespondedApplicant response = respondedApplicantService
                .getByResumeIdAndVacancyId(resume.getId(), vacancy.getId())
                .orElseGet(() -> {
                    RespondToVacancyDto dto = new RespondToVacancyDto();
                    dto.setResumeId(resume.getId());
                    dto.setVacancyId(vacancy.getId());

                    return respondedApplicantService.create(dto);
                });

        return "redirect:/chat/" + response.getId();
    }

    @GetMapping("/resumes/create")
    public String createResumePage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can create resumes");
        }

        model.addAttribute("resume", new CreateResumeDto());
        fillResumeFormModel(currentUser, model, null);

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

        validateCreateResume(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            fillResumeFormModel(currentUser, model, null);
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
                .orElseThrow(ResumeNotFoundException::new);

        if (!existingResume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can edit only your own resume");
        }

        List<ContactInfo> contactInfos = contactInfoRepository.findByResumeId(id);
        List<EducationInfo> educationInfos = educationInfoRepository.findByResumeId(id);
        List<WorkExperienceInfo> workExperienceInfos = workExperienceInfoRepository.findByResumeId(id);

        UpdateResumeDto dto = UpdateResumeDto.builder()
                .name(existingResume.getName())
                .categoryId(existingResume.getCategoryId())
                .salary(existingResume.getSalary())
                .isActive(existingResume.getIsActive())
                .contactInfos(contactInfos.stream()
                        .map(contactInfo -> new UpdateResumeDto.ContactDto(
                                contactInfo.getType() != null ? contactInfo.getType().getId() : null,
                                contactInfo.getContactValue()
                        ))
                        .collect(Collectors.toCollection(ArrayList::new)))
                .educationInfos(educationInfos.stream()
                        .map(educationInfo -> new UpdateResumeDto.EducationDto(
                                educationInfo.getInstitution(),
                                educationInfo.getProgram(),
                                educationInfo.getStartDate() != null ? educationInfo.getStartDate().toString() : null,
                                educationInfo.getEndDate() != null ? educationInfo.getEndDate().toString() : null,
                                educationInfo.getDegree()
                        ))
                        .collect(Collectors.toCollection(ArrayList::new)))
                .workExperienceInfos(workExperienceInfos.stream()
                        .map(workExperienceInfo -> new UpdateResumeDto.WorkExperienceDto(
                                workExperienceInfo.getYears(),
                                workExperienceInfo.getCompanyName(),
                                workExperienceInfo.getPosition(),
                                workExperienceInfo.getResponsibilities()
                        ))
                        .collect(Collectors.toCollection(ArrayList::new)))
                .build();

        model.addAttribute("resume", dto);
        fillResumeFormModel(currentUser, model, id);

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
                .orElseThrow(ResumeNotFoundException::new);

        if (!existingResume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can edit only your own resume");
        }

        validateUpdateResume(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            fillResumeFormModel(currentUser, model, id);
            return "resume-form";
        }

        resumeService.update(id, dto);

        return "redirect:/resumes";
    }

    @GetMapping("/employer/resumes")
    public String employerResumesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Integer categoryId,
            Authentication authentication,
            Model model
    ) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can view all resumes");
        }

        Page<Resume> resumePage;

        if (categoryId != null) {
            resumePage = resumeService.getActiveByCategory(categoryId, page, size);
        } else {
            resumePage = resumeService.getAllActive(page, size);
        }

        List<Resume> resumes = resumePage.getContent();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resumes", resumes);
        model.addAttribute("resumeUpdateTimes", buildResumeUpdateTimeMap(resumes));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resumePage.getTotalPages());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("categoryId", categoryId);

        return "employer-resume-list";
    }
}