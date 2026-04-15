package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.UpdateProfileDto;
import kg.attractor.job_search.dto.UpdateUserDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.FileUploadException;
import kg.attractor.job_search.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private Map<Integer, String> buildResumeUpdateTimeMap(List<Resume> resumes) {
        Map<Integer, String> formattedDates = new LinkedHashMap<>();

        for (Resume resume : resumes) {
            formattedDates.put(resume.getId(), formatDateTime(resume.getUpdateTime()));
        }

        return formattedDates;
    }

    private Map<Integer, String> buildVacancyUpdateTimeMap(List<Vacancy> vacancies) {
        Map<Integer, String> formattedDates = new LinkedHashMap<>();

        for (Vacancy vacancy : vacancies) {
            formattedDates.put(vacancy.getId(), formatDateTime(vacancy.getUpdateTime()));
        }

        return formattedDates;
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);

        model.addAttribute("user", currentUser);
        model.addAttribute("currentUser", currentUser);

        if ("APPLICANT".equals(currentUser.getAccountType().name())) {
            List<Resume> resumes = resumeService.getByApplicantId(currentUser.getId());
            model.addAttribute("resumes", resumes);
            model.addAttribute("resumeUpdateTimes", buildResumeUpdateTimeMap(resumes));
        } else {
            List<Vacancy> vacancies = vacancyService.getByAuthorId(currentUser.getId());
            model.addAttribute("vacancies", vacancies);
            model.addAttribute("vacancyUpdateTimes", buildVacancyUpdateTimeMap(vacancies));
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

    @PostMapping("/profile/avatar")
    public String uploadAvatar(MultipartFile file, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        try {
            String fileName = fileService.saveUploadedFile(file, "avatars");

            userService.updateAvatar(currentUser.getId(), fileName)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            return "redirect:/profile";
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload avatar");
        }
    }
}
