package kg.attractor.job_search.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LocaleController {

    private final UserService userService;

    @PostMapping("/change-language")
    public String changeLanguage(@RequestParam String lang,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        if (!"ru".equals(lang) && !"en".equals(lang)) {
            lang = "ru";
        }

        String selectedLang = lang;

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.findByEmail(email)
                .ifPresent(currentUser -> userService.updateLocale(currentUser.getId(), selectedLang));

        Cookie cookie = new Cookie("user-lang", lang);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(cookie);

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }

        return "redirect:/vacancies";
    }
}