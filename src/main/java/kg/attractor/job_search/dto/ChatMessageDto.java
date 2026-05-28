package kg.attractor.job_search.dto;

import kg.attractor.job_search.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Integer respondedApplicantId;
    private String content;
    private String senderName;
    private String timestamp;

    public static ChatMessageDto fromMessage(Message message) {
        String senderName = "";

        if (message.getSender() != null) {
            senderName = message.getSender().getName() + " " + message.getSender().getSurname();
        }

        String timestamp = "";

        if (message.getTimestamp() != null) {
            timestamp = message.getTimestamp().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }

        return new ChatMessageDto(
                message.getRespondedApplicant().getId(),
                message.getContent(),
                senderName,
                timestamp
        );
    }
}