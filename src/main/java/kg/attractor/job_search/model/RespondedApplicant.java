package kg.attractor.job_search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespondedApplicant {
    private int id;
    private Integer resumeId;
    private Integer vacancyId;
    private boolean confirmation;
}

