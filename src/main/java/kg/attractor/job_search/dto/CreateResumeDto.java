package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateResumeDto {

    @NotBlank(message = "Название резюме не может быть пустым")
    private String name;

    @NotNull(message = "Категория должна быть выбрана")
    @Positive(message = "Категория должна быть выбрана")
    private Integer categoryId;

    @NotNull(message = "Зарплата не может быть пустой")
    @PositiveOrZero(message = "Зарплата не может быть отрицательной")
    private Double salary;

    private Boolean isActive;
}