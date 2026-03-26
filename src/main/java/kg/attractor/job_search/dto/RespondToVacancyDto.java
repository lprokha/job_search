package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespondToVacancyDto {

    @NotNull(message = "Resume id cannot be null")
    @Positive(message = "Resume id must be positive")
    private Integer resumeId;

    @NotNull(message = "Vacancy id cannot be null")
    @Positive(message = "Vacancy id must be positive")
    private Integer vacancyId;
}