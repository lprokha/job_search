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
public class UpdateResumeDto {

    @NotBlank(message = "Название резюме не может быть пустым")
    private String name;

    @NotNull(message = "Категория должна быть выбрана")
    @Positive(message = "Категория должна быть выбрана")
    private Integer categoryId;

    @NotNull(message = "Зарплата не может быть пустой")
    @PositiveOrZero(message = "Зарплата не может быть отрицательной")
    private Double salary;

    @NotNull(message = "Статус активности должен быть выбран")
    private Boolean isActive;

    @NotNull(message = "Тип контакта должен быть выбран")
    @Positive(message = "Тип контакта должен быть выбран")
    private Integer contactTypeId;

    @NotBlank(message = "Контакт не может быть пустым")
    private String contactValue;

    @NotBlank(message = "Учебное заведение не может быть пустым")
    private String institution;

    private String program;
    private String startDate;
    private String endDate;
    private String degree;

    private Integer years;
    private String companyName;
    private String position;
    private String responsibilities;
}