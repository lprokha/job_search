package kg.attractor.job_search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}