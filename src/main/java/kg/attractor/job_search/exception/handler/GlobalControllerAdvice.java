package kg.attractor.job_search.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.ConflictException;
import kg.attractor.job_search.exception.FileUploadException;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
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

    private String errorPage(HttpServletResponse response,
                             Model model,
                             HttpStatus status,
                             String reason,
                             HttpServletRequest request) {
        response.setStatus(status.value());
        addCurrentUser(model);
        model.addAttribute("status", status.value());
        model.addAttribute("reason", reason);
        model.addAttribute("details", request);

        return "errors/error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFound(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Model model) {
        return errorPage(
                response,
                model,
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase() + ": Page not found",
                request
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFound(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Model model) {
        return errorPage(
                response,
                model,
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase() + ": Resource not found",
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Model model,
                                     UserNotFoundException e) {
        return errorPage(
                response,
                model,
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model,
                                 NotFoundException e) {
        return errorPage(
                response,
                model,
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase() + ": " + e.getMessage(),
                request
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequest(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Model model,
                                   BadRequestException e) {
        return errorPage(
                response,
                model,
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase() + ": " + e.getMessage(),
                request
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleForbidden(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Model model,
                                  ForbiddenException e) {
        return errorPage(
                response,
                model,
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.getReasonPhrase() + ": " + e.getMessage(),
                request
        );
    }

    @ExceptionHandler(ConflictException.class)
    public String handleConflict(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model,
                                 ConflictException e) {
        return errorPage(
                response,
                model,
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase() + ": " + e.getMessage(),
                request
        );
    }

    @ExceptionHandler(FileUploadException.class)
    public String handleFileUpload(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Model model,
                                   FileUploadException e) {
        return errorPage(
                response,
                model,
                HttpStatus.BAD_REQUEST,
                "Ошибка загрузки файла: " + e.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherExceptions(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Model model,
                                        Exception e) {
        log.error("Unhandled exception occurred", e);

        return errorPage(
                response,
                model,
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                request
        );
    }
}