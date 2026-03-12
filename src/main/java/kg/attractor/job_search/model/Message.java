package kg.attractor.job_search.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private int od;
    private int respondedApplicantId;
    private String content;
    private LocalDateTime timestamp;
}
