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
public class UpdateProfileDto {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String surname;

    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 16, message = "Возраст должен быть не меньше 16")
    @Max(value = 100, message = "Возраст должен быть не больше 100")
    private Integer age;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(
            regexp = "^\\+?\\d{10,15}$",
            message = "Телефон должен содержать от 10 до 15 цифр и может начинаться с +"
    )
    private String phoneNumber;
}