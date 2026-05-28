package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.exception.VacancyNotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class VacancyPageController {

    private final VacancyService vacancyService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ResumeService resumeService;
    private final RespondedApplicantService respondedApplicantService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private User getCurrentUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName());
    }

    private User addCurrentUserIfAuthenticated(Authentication authentication, Model model) {
        if (!isAuthenticated(authentication)) {
            return null;
        }

        User currentUser = getCurrentUser(authentication);
        model.addAttribute("currentUser", currentUser);

        return currentUser;
    }

    private void forbidEmployer(User currentUser, String message) {
        if (currentUser != null && currentUser.getAccountType() == AccountType.EMPLOYER) {
            throw new ForbiddenException(message);
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private Map<String, String> buildVacancyUpdateTimeMap(List<Vacancy> vacancies) {
        Map<String, String> formattedDates = new LinkedHashMap<>();

        for (Vacancy vacancy : vacancies) {
            formattedDates.put(vacancy.getId().toString(), formatDateTime(vacancy.getUpdateTime()));
        }

        return formattedDates;
    }

    @GetMapping("/vacancies")
    public String vacanciesPage(
            @RequestParam(defaultValue = "dateDesc") String sort,
            Authentication authentication,
            Model model
    ) {
        User currentUser = addCurrentUserIfAuthenticated(authentication, model);
        forbidEmployer(currentUser, "Employers cannot view all vacancies");

        Page<Vacancy> vacancyPage = vacancyService.getAllActive(0, 100, sort);
        List<Vacancy> vacancies = vacancyPage.getContent();

        model.addAttribute("vacancies", vacancies);
        model.addAttribute("vacancyUpdateTimes", buildVacancyUpdateTimeMap(vacancies));
        model.addAttribute("sort", sort);
        model.addAttribute("categories", categoryService.getAll());

        return "vacancy-list";
    }

    @GetMapping("/vacancies/{id}")
    public String vacancyDetailPage(@PathVariable Integer id,
                                    Authentication authentication,
                                    Model model) {
        User currentUser = addCurrentUserIfAuthenticated(authentication, model);
        forbidEmployer(currentUser, "Employers cannot view vacancy details");

        Vacancy vacancy = vacancyService.getById(id)
                .orElseThrow(VacancyNotFoundException::new);

        model.addAttribute("vacancy", vacancy);
        model.addAttribute("updatedAt", formatDateTime(vacancy.getUpdateTime()));
        model.addAttribute("respondDto", new RespondToVacancyDto());

        if (currentUser != null && currentUser.getAccountType() == AccountType.APPLICANT) {
            List<Resume> resumes = resumeService.getByApplicantId(currentUser.getId()).stream()
                    .filter(resume -> Boolean.TRUE.equals(resume.getIsActive()))
                    .toList();

            model.addAttribute("resumes", resumes);
        }

        return "vacancy-detail";
    }

    @PostMapping("/vacancies/{id}/respond")
    public String respondToVacancy(@PathVariable Integer id,
                                   @Valid @ModelAttribute("respondDto") RespondToVacancyDto dto,
                                   BindingResult bindingResult,
                                   Authentication authentication,
                                   Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can respond to vacancies");
        }

        Vacancy vacancy = vacancyService.getById(id)
                .orElseThrow(VacancyNotFoundException::new);

        Resume resume = resumeService.getById(dto.getResumeId())
                .orElseThrow(() -> new NotFoundException("Resume not found"));

        if (!resume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can respond only with your own resume");
        }

        dto.setVacancyId(vacancy.getId());

        RespondedApplicant response = respondedApplicantService
                .getByResumeIdAndVacancyId(dto.getResumeId(), vacancy.getId())
                .orElseGet(() -> respondedApplicantService.create(dto));

        return "redirect:/chat/" + response.getId();
    }

    @GetMapping("/companies")
    public String companiesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Authentication authentication,
            Model model
    ) {
        User currentUser = addCurrentUserIfAuthenticated(authentication, model);
        forbidEmployer(currentUser, "Employers cannot view companies");

        List<User> allEmployers = userService.getAllEmployers();

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allEmployers.size());

        List<User> companies = fromIndex >= allEmployers.size()
                ? List.of()
                : allEmployers.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) allEmployers.size() / size);

        Map<String, Integer> companyVacancyCounts = new LinkedHashMap<>();
        for (User company : companies) {
            companyVacancyCounts.put(company.getId().toString(), vacancyService.getByAuthorId(company.getId()).size());
        }

        model.addAttribute("companies", companies);
        model.addAttribute("companyVacancyCounts", companyVacancyCounts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "companies";
    }

    @GetMapping("/companies/{id}")
    public String companyVacanciesPage(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "dateDesc") String sort,
            Authentication authentication,
            Model model
    ) {
        User currentUser = addCurrentUserIfAuthenticated(authentication, model);
        forbidEmployer(currentUser, "Employers cannot view companies");

        User company = userService.findEmployer(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        Page<Vacancy> vacancyPage = vacancyService.getByAuthorId(id, page, size, sort);
        List<Vacancy> vacancies = vacancyPage.getContent();

        model.addAttribute("company", company);
        model.addAttribute("vacancies", vacancies);
        model.addAttribute("vacancyUpdateTimes", buildVacancyUpdateTimeMap(vacancies));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacancyPage.getTotalPages());
        model.addAttribute("sort", sort);

        return "company-vacancies";
    }

    @GetMapping("/my-vacancies")
    public String myVacanciesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "dateDesc") String sort,
            Authentication authentication,
            Model model
    ) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can view their vacancies");
        }

        Page<Vacancy> vacancyPage = vacancyService.getByAuthorId(currentUser.getId(), page, size, sort);
        List<Vacancy> vacancies = vacancyPage.getContent();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("vacancies", vacancies);
        model.addAttribute("vacancyUpdateTimes", buildVacancyUpdateTimeMap(vacancies));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacancyPage.getTotalPages());
        model.addAttribute("sort", sort);

        return "my-vacancies";
    }

    @GetMapping("/my-vacancies/create")
    public String createVacancyPage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can create vacancies");
        }

        model.addAttribute("vacancy", new CreateVacancyDto());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("currentUser", currentUser);

        return "vacancy-form";
    }

    @PostMapping("/my-vacancies/create")
    public String createVacancy(
            @Valid @ModelAttribute("vacancy") CreateVacancyDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can create vacancies");
        }

        if (dto.getExpFrom() != null && dto.getExpTo() != null && dto.getExpTo() <= dto.getExpFrom()) {
            bindingResult.rejectValue("expTo", "error.vacancy", "Опыт до должен быть больше опыта от");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("currentUser", currentUser);
            return "vacancy-form";
        }

        vacancyService.create(dto, currentUser.getId());
        return "redirect:/my-vacancies";
    }

    @GetMapping("/my-vacancies/edit/{id}")
    public String editVacancyPage(@PathVariable Integer id, Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can edit vacancies");
        }

        Vacancy existingVacancy = vacancyService.getById(id)
                .orElseThrow(VacancyNotFoundException::new);

        if (!existingVacancy.getAuthorId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can edit only your own vacancy");
        }

        UpdateVacancyDto dto = UpdateVacancyDto.builder()
                .name(existingVacancy.getName())
                .description(existingVacancy.getDescription())
                .categoryId(existingVacancy.getCategoryId())
                .salary(existingVacancy.getSalary())
                .expFrom(existingVacancy.getExpFrom())
                .expTo(existingVacancy.getExpTo())
                .isActive(existingVacancy.getIsActive())
                .build();

        model.addAttribute("vacancy", dto);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("vacancyId", id);

        return "vacancy-form";
    }

    @PostMapping("/my-vacancies/edit/{id}")
    public String editVacancy(
            @PathVariable Integer id,
            @Valid @ModelAttribute("vacancy") UpdateVacancyDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can edit vacancies");
        }

        Vacancy existingVacancy = vacancyService.getById(id)
                .orElseThrow(VacancyNotFoundException::new);

        if (!existingVacancy.getAuthorId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can edit only your own vacancy");
        }

        if (dto.getExpFrom() != null && dto.getExpTo() != null && dto.getExpTo() <= dto.getExpFrom()) {
            bindingResult.rejectValue("expTo", "error.vacancy", "Опыт до должен быть больше опыта от");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("vacancyId", id);
            return "vacancy-form";
        }

        vacancyService.update(id, dto);
        return "redirect:/my-vacancies";
    }
}