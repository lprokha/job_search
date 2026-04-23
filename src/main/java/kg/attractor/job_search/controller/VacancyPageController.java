package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.CategoryService;
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

    private Map<String, String> buildVacancyUpdateTimeMap(List<Vacancy> vacancies) {
        Map<String, String> formattedDates = new LinkedHashMap<>();

        for (Vacancy vacancy : vacancies) {
            formattedDates.put(vacancy.getId().toString(), formatDateTime(vacancy.getUpdateTime()));
        }

        return formattedDates;
    }

    @GetMapping("/vacancies")
    public String vacanciesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "date") String sort,
            Authentication authentication,
            Model model
    ) {
        Page<Vacancy> vacancyPage = vacancyService.getAllActive(page, size, sort);
        List<Vacancy> vacancies = vacancyPage.getContent();

        model.addAttribute("vacancies", vacancies);
        model.addAttribute("vacancyUpdateTimes", buildVacancyUpdateTimeMap(vacancies));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacancyPage.getTotalPages());
        model.addAttribute("sort", sort);

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            User currentUser = getCurrentUser(authentication);
            model.addAttribute("currentUser", currentUser);
        }

        return "vacancy-list";
    }

    @GetMapping("/companies")
    public String companies(Model model) {
        model.addAttribute("companies", userService.getAllEmployers());
        return "companies";
    }

    @GetMapping("/my-vacancies")
    public String myVacanciesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "date") String sort,
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
                .orElseThrow(() -> new NotFoundException("Vacancy not found with id = " + id));

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
                .orElseThrow(() -> new NotFoundException("Vacancy not found with id = " + id));

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