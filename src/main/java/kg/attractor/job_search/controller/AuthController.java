package kg.attractor.job_search.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}