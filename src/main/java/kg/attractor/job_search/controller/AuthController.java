package kg.attractor.job_search.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.dto.ResetPasswordDto;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new CreateUserDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("user") CreateUserDto dto,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request
    ) throws ServletException {
        if (userService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Пользователь с таким email уже существует");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.create(dto);

        request.login(dto.getEmail(), dto.getPassword());

        if (dto.getAccountType() == AccountType.EMPLOYER) {
            return "redirect:/employer/resumes";
        }

        return "redirect:/vacancies";
    }

    @GetMapping("/forgot-password")
    public String showForgotPwd() {
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        try{
            userService.makeResetPwdLink(request);
            model.addAttribute("message", "На ваш email отправлена ссылка для смены пароля");
        } catch (NotFoundException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException e) {
            model.addAttribute("error", "Ошибка отправки письма");
        }
        return "forgot-password-form";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        try {
            userService.getByResetPasswordToken(token);
            ResetPasswordDto form = new ResetPasswordDto();
            form.setToken(token);
            model.addAttribute("form", form);
        } catch (NotFoundException e) {
            model.addAttribute("error", "Invalid token!");
        }

        return "reset-password-form";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @Valid @ModelAttribute("form") ResetPasswordDto form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("token", form.getToken());
            return "reset-password-form";
        }

        try {
            User user = userService.getByResetPasswordToken(form.getToken());
            userService.updatePassword(user, form.getPassword());
            model.addAttribute("message", "Пароль успешно изменен");
        } catch (NotFoundException e) {
            model.addAttribute("message", "Invalid token");
        }

        return "partial/message";
    }
}