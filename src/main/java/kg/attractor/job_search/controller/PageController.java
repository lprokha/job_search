package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.UpdateProfileDto;
import kg.attractor.job_search.dto.UpdateUserDto;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    private User getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);

        model.addAttribute("user", user);

        if ("APPLICANT".equals(user.getAccountType().name())) {
            model.addAttribute("resumes", resumeService.getByApplicantId(user.getId()));
        } else {
            model.addAttribute("vacancies", vacancyService.getByAuthorId(user.getId()));
        }

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(Authentication authentication, Model model) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name(currentUser.getName())
                .surname(currentUser.getSurname())
                .age(currentUser.getAge())
                .email(currentUser.getEmail())
                .phoneNumber(currentUser.getPhoneNumber())
                .build();

        model.addAttribute("user", dto);
        model.addAttribute("currentUser", currentUser);

        return "edit-user";
    }

    @GetMapping("/resumes")
    public String resumesPage(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        model.addAttribute("user", user);
        model.addAttribute("resumes", resumeService.getByApplicantId(user.getId()));
        return "resume-list";
    }

    @GetMapping("/my-vacancies")
    public String myVacanciesPage(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        model.addAttribute("user", user);
        model.addAttribute("vacancies", vacancyService.getByAuthorId(user.getId()));
        return "my-vacancies";
    }

    @GetMapping("/vacancies")
    public String vacanciesPage(Authentication authentication, Model model) {
        model.addAttribute("vacancies", vacancyService.getAllActive());

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            User user = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            model.addAttribute("user", user);
        }

        return "vacancy-list";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @Valid @ModelAttribute("user") UpdateProfileDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentUser", currentUser);
            return "edit-user";
        }

        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .age(dto.getAge())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .password(null)
                .build();

        userService.updateProfile(currentUser.getId(), updateUserDto);

        return "redirect:/profile";
    }
}