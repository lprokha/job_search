package kg.attractor.job_search.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EducationInfo {
    private int id;
    private int resumeId;
    private String institution;
    private String program;
    private LocalDateTime stratDate;
    private LocalDateTime endDate;
    private String degree;
}
