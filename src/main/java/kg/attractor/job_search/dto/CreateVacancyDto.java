package kg.attractor.job_search.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVacancyDto {

    @NotBlank(message = "{validation.vacancy.name.notBlank}")
    private String name;

    @NotBlank(message = "{validation.vacancy.description.notBlank}")
    private String description;

    @NotNull(message = "{validation.category.notNull}")
    private Integer categoryId;

    @NotNull(message = "{validation.salary.notNull}")
    @Positive(message = "{validation.salary.positive}")
    private Double salary;

    @NotNull(message = "{validation.vacancy.expFrom.notNull}")
    @Min(value = 0, message = "{validation.vacancy.expFrom.min}")
    private Integer expFrom;

    @NotNull(message = "{validation.vacancy.expTo.notNull}")
    @Min(value = 0, message = "{validation.vacancy.expTo.min}")
    private Integer expTo;

    @NotNull(message = "{validation.status.notNull}")
    private Boolean isActive;
}