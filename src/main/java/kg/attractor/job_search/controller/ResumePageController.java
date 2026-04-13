package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
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

@Controller
@RequiredArgsConstructor
public class ResumePageController {

    private final ResumeService resumeService;
    private final UserService userService;
    private final CategoryService categoryService;

    private User getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @GetMapping("/resumes/create")
    public String createResumePage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can create resumes");
        }

        model.addAttribute("resume", new CreateResumeDto());
        model.addAttribute("categories", categoryService.getAll());
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
            model.addAttribute("currentUser", currentUser);
            return "resume-form";
        }

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

        UpdateResumeDto dto = UpdateResumeDto.builder()
                .name(existingResume.getName())
                .categoryId(existingResume.getCategoryId())
                .salary(existingResume.getSalary())
                .isActive(existingResume.getIsActive())
                .build();

        model.addAttribute("resume", dto);
        model.addAttribute("categories", categoryService.getAll());
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

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resumes", resumeService.getAll());

        return "employer-resume-list";
    }
}