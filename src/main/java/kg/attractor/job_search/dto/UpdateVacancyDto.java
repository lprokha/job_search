package kg.attractor.job_search.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVacancyDto {

    @NotNull(message = "Author id cannot be null")
    @Positive(message = "Author id must be positive")
    private Integer authorId;

    @NotBlank(message = "Vacancy name cannot be empty")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Category id cannot be null")
    @Positive(message = "Category id must be positive")
    private Integer categoryId;

    @NotNull(message = "Salary cannot be null")
    @PositiveOrZero(message = "Salary cannot be negative")
    private Double salary;

    @NotNull(message = "Experience from cannot be null")
    @PositiveOrZero(message = "Experience from cannot be negative")
    private Integer expFrom;

    @NotNull(message = "Experience to cannot be null")
    @PositiveOrZero(message = "Experience to cannot be negative")
    private Integer expTo;

    @NotNull(message = "Active flag cannot be null")
    private Boolean isActive;
}