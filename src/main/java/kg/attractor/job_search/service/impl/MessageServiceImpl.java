package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.Message;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.repository.MessageRepository;
import kg.attractor.job_search.repository.RespondedApplicantRepository;
import kg.attractor.job_search.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final RespondedApplicantRepository respondedApplicantRepository;

    @Override
    public Message create(Integer respondedApplicantId, String content, User sender) {
        RespondedApplicant respondedApplicant = respondedApplicantRepository.findById(respondedApplicantId)
                .orElseThrow(() -> new NotFoundException("Response not found"));

        Message message = new Message();
        message.setRespondedApplicant(respondedApplicant);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        log.debug("Message created, id={}, responseId={}", savedMessage.getId(), respondedApplicantId);

        return savedMessage;
    }

    @Override
    public List<Message> getByRespondedApplicantId(Integer respondedApplicantId) {
        return messageRepository.findByRespondedApplicant_IdOrderByTimestampAsc(respondedApplicantId);
    }
}