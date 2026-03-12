package kg.attractor.job_search.model;

import lombok.Data;

@Data
public class RespondedApplicant {
    private int id;
    private int resumeId;
    private int vacancyId;
    private boolean confirmation;
}

