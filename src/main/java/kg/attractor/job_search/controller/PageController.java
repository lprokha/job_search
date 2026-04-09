package kg.attractor.job_search.controller;

import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        User user = getCurrentUser(authentication);
        model.addAttribute("user", user);
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
}