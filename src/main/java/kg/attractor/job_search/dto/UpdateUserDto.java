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
public class UpdateUserDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    @NotNull(message = "Age cannot be null")
    @Min(value = 16, message = "Age must be at least 16")
    @Max(value = 100, message = "Age must be no more than 100")
    private Integer age;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is invalid")
    private String email;

    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(
            regexp = "^\\+?\\d{10,15}$",
            message = "Phone number must contain from 10 to 15 digits and may start with +"
    )
    private String phoneNumber;
}