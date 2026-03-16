package kg.attractor.job_search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespondToVacancyDto {
    private Integer resumeId;
    private Integer vacancyId;
}