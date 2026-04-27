package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 4, max = 24, message = "Пароль должен быть от 4 до 24 символов")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).+$",
            message = "Пароль должен содержать хотя бы одну цифру, одну строчную и одну заглавную букву"
    )
    private String password;

    private String token;
}