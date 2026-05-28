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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final RespondedApplicantService respondedApplicantService;
    private final UserService userService;

    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessageDto dto, Principal principal) {
        if (principal == null) {
            throw new ForbiddenException("Only authenticated users can send messages");
        }

        User currentUser = userService.findByEmail(principal.getName())
                .orElseThrow(UserNotFoundException::new);

        RespondedApplicant response = respondedApplicantService.getById(dto.getRespondedApplicantId())
                .orElseThrow(() -> new NotFoundException("Response not found"));

        if (!canSendMessage(currentUser, response)) {
            throw new ForbiddenException("You cannot send message to this chat");
        }

        if (dto.getContent() == null || dto.getContent().isBlank()) {
            return;
        }

        Message message = messageService.create(
                dto.getRespondedApplicantId(),
                dto.getContent(),
                currentUser
        );

        messagingTemplate.convertAndSend(
                "/topic/chat/" + dto.getRespondedApplicantId(),
                ChatMessageDto.fromMessage(message)
        );
    }

    private boolean canSendMessage(User currentUser, RespondedApplicant response) {
        boolean isApplicant = response.getResume() != null
                && response.getResume().getApplicant() != null
                && response.getResume().getApplicant().getId().equals(currentUser.getId());

        boolean isEmployer = response.getVacancy() != null
                && response.getVacancy().getAuthor() != null
                && response.getVacancy().getAuthor().getId().equals(currentUser.getId());

        return isApplicant || isEmployer;
    }
}