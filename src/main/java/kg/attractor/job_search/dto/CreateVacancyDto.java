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
public class CreateVacancyDto {

    @NotBlank(message = "Название вакансии не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Категория должна быть выбрана")
    @Positive(message = "Категория должна быть выбрана")
    private Integer categoryId;

    @NotNull(message = "Зарплата не может быть пустой")
    @PositiveOrZero(message = "Зарплата не может быть отрицательной")
    private Double salary;

    @NotNull(message = "Опыт от не может быть пустым")
    @PositiveOrZero(message = "Опыт от не может быть отрицательным")
    private Integer expFrom;

    @NotNull(message = "Опыт до не может быть пустым")
    @PositiveOrZero(message = "Опыт до не может быть отрицательным")
    private Integer expTo;

    @NotNull(message = "Статус активности должен быть выбран")
    private Boolean isActive;
}