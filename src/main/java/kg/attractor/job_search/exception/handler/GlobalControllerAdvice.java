package kg.attractor.job_search.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.ConflictException;
import kg.attractor.job_search.exception.FileUploadException;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserService userService;

    private void addCurrentUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            userService.findByEmail(authentication.getName())
                    .ifPresent(user -> model.addAttribute("currentUser", user));
        }
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(HttpServletRequest request, Model model, NotFoundException e) {
        addCurrentUser(model);
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("reason", HttpStatus.NOT_FOUND.getReasonPhrase() + ": " + e.getMessage());
        model.addAttribute("details", request);
        return "errors/error";
    }

    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequest(HttpServletRequest request, Model model, BadRequestException e) {
        addCurrentUser(model);
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("reason", HttpStatus.BAD_REQUEST.getReasonPhrase() + ": " + e.getMessage());
        model.addAttribute("details", request);
        return "errors/error";
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleForbidden(HttpServletRequest request, Model model, ForbiddenException e) {
        addCurrentUser(model);
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("reason", HttpStatus.FORBIDDEN.getReasonPhrase() + ": " + e.getMessage());
        model.addAttribute("details", request);
        return "errors/error";
    }

    @ExceptionHandler(ConflictException.class)
    public String handleConflict(HttpServletRequest request, Model model, ConflictException e) {
        addCurrentUser(model);
        model.addAttribute("status", HttpStatus.CONFLICT.value());
        model.addAttribute("reason", HttpStatus.CONFLICT.getReasonPhrase() + ": " + e.getMessage());
        model.addAttribute("details", request);
        return "errors/error";
    }

    @ExceptionHandler(FileUploadException.class)
    public String handleFileUpload(HttpServletRequest request, Model model, FileUploadException e) {
        addCurrentUser(model);
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("reason", "File upload error: " + e.getMessage());
        model.addAttribute("details", request);
        return "errors/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherExceptions(HttpServletRequest request, Model model, Exception e) {
        addCurrentUser(model);
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("reason", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("details", request);
        return "errors/error";
    }
}