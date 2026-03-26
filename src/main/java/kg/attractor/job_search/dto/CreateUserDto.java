package kg.attractor.job_search.dto;

import jakarta.validation.constraints.*;
import kg.attractor.job_search.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
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

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, max = 24, message = "Password must be from 4 to 24 characters long")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).+$",
            message = "Password must contain at least one lowercase letter, one uppercase letter and one digit")
    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?\\d{10,15}$",
            message = "Phone nubmer must contain from 10 to 15 digits and may start with +")
    private String phoneNumber;

    @NotNull(message = "Account type must be selected")
    private AccountType accountType;
}