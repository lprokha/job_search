package kg.attractor.job_search.controller;

import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.exception.VacancyNotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.RespondedApplicantService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ResponsePageController {

    private final RespondedApplicantService respondedApplicantService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final UserService userService;

    private User getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/responses/my")
    public String myResponsesPage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can view their responses");
        }

        List<Resume> resumes = resumeService.getByApplicantId(currentUser.getId());
        List<RespondedApplicant> responses = new ArrayList<>();

        for (Resume resume : resumes) {
            responses.addAll(respondedApplicantService.getByResumeId(resume.getId()));
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("responses", responses);

        return "my-responses";
    }

    @GetMapping("/my-vacancies/{vacancyId}/responses")
    public String vacancyResponsesPage(@PathVariable Integer vacancyId,
                                       Authentication authentication,
                                       Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can view vacancy responses");
        }

        Vacancy vacancy = vacancyService.getById(vacancyId)
                .orElseThrow(VacancyNotFoundException::new);

        if (!vacancy.getAuthorId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can view responses only for your own vacancies");
        }

        List<RespondedApplicant> responses = respondedApplicantService.getByVacancyId(vacancyId);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("vacancy", vacancy);
        model.addAttribute("responses", responses);

        return "vacancy-responses";
    }
}