package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.ChatMessageDto;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.model.Message;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.MessageService;
import kg.attractor.job_search.service.RespondedApplicantService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatPageController {

    private final RespondedApplicantService respondedApplicantService;
    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/chat/{responseId}")
    public String chatPage(@PathVariable Integer responseId,
                           Authentication authentication,
                           Model model) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        RespondedApplicant response = respondedApplicantService.getById(responseId)
                .orElseThrow(() -> new NotFoundException("Response not found"));

        if (!canOpenChat(currentUser, response)) {
            throw new ForbiddenException("You cannot open this chat");
        }

        List<Message> messages = messageService.getByRespondedApplicantId(responseId);

        List<ChatMessageDto> messageDtos = messages.stream()
                .map(ChatMessageDto::fromMessage)
                .toList();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("response", response);
        model.addAttribute("messages", messageDtos);

        return "chat";
    }

    private boolean canOpenChat(User currentUser, RespondedApplicant response) {
        boolean isApplicant = response.getResume() != null
                && response.getResume().getApplicant() != null
                && response.getResume().getApplicant().getId().equals(currentUser.getId());

        boolean isEmployer = response.getVacancy() != null
                && response.getVacancy().getAuthor() != null
                && response.getVacancy().getAuthor().getId().equals(currentUser.getId());

        return isApplicant || isEmployer;
    }
}