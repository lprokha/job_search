package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.UpdateProfileDto;
import kg.attractor.job_search.dto.UpdateUserDto;
import kg.attractor.job_search.exception.FileUploadException;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.exception.VacancyNotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.FileService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProfilePageController {
    private final UserService userService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final FileService fileService;

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

    private Map<String, String> buildVacancyUpdateTimeMap(List<Vacancy> vacancies) {
        Map<String, String> formattedDates = new LinkedHashMap<>();

        for (Vacancy vacancy : vacancies) {
            formattedDates.put(vacancy.getId().toString(), formatDateTime(vacancy.getUpdateTime()));
        }

        return formattedDates;
    }

    private UpdateProfileDto buildUpdateProfileDto(User currentUser) {
        return UpdateProfileDto.builder()
                .name(currentUser.getName())
                .surname(currentUser.getSurname())
                .age(currentUser.getAge())
                .email(currentUser.getEmail())
                .phoneNumber(currentUser.getPhoneNumber())
                .build();
    }

    private void fillProfileModel(User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        model.addAttribute("currentUser", currentUser);

        if (!model.containsAttribute("profileForm")) {
            model.addAttribute("profileForm", buildUpdateProfileDto(currentUser));
        }

        if (currentUser.getAccountType() == AccountType.APPLICANT) {
            List<Resume> resumes = resumeService.getByApplicantId(currentUser.getId());
            model.addAttribute("resumes", resumes);
            model.addAttribute("resumeUpdateTimes", buildResumeUpdateTimeMap(resumes));
        } else {
            List<Vacancy> vacancies = vacancyService.getByAuthorId(currentUser.getId());
            model.addAttribute("vacancies", vacancies);
            model.addAttribute("vacancyUpdateTimes", buildVacancyUpdateTimeMap(vacancies));
        }
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);
        fillProfileModel(currentUser, model);

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);
        fillProfileModel(currentUser, model);
        model.addAttribute("showEditProfileModal", true);

        return "profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @Valid @ModelAttribute("profileForm") UpdateProfileDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        User currentUser = getCurrentUser(authentication);

        if (bindingResult.hasErrors()) {
            fillProfileModel(currentUser, model);
            model.addAttribute("showEditProfileModal", true);
            return "profile";
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

    @PostMapping("/profile/avatar")
    public String uploadAvatar(MultipartFile file, Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (file == null || file.isEmpty()) {
            fillProfileModel(currentUser, model);
            model.addAttribute("avatarError", "Сначала выберите фото для загрузки");

            return "profile";
        }

        try {
            String fileName = fileService.saveUploadedFile(file, "avatars");

            userService.updateAvatar(currentUser.getId(), fileName)
                    .orElseThrow(UserNotFoundException::new);

            return "redirect:/profile";
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload avatar");
        }
    }

    @PostMapping("/resumes/{id}/refresh")
    public String refreshResume(@PathVariable Integer id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can refresh resumes");
        }

        Resume resume = resumeService.getById(id)
                .orElseThrow(UserNotFoundException::new);

        if (!resume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can refresh only your own resume");
        }

        resumeService.refresh(id);

        return "redirect:/profile";
    }

    @PostMapping("/my-vacancies/{id}/refresh")
    public String refreshVacancy(@PathVariable Integer id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser.getAccountType() != AccountType.EMPLOYER) {
            throw new ForbiddenException("Only employers can refresh vacancies");
        }

        Vacancy vacancy = vacancyService.getById(id)
                .orElseThrow(VacancyNotFoundException::new);

        if (!vacancy.getAuthorId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can refresh only your own vacancy");
        }

        vacancyService.refresh(id);

        return "redirect:/profile";
    }
}