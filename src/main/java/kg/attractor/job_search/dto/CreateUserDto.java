package kg.attractor.job_search.dto;

import jakarta.validation.constraints.*;
import kg.attractor.job_search.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "{validation.name.notBlank}")
    private String name;

    @NotBlank(message = "{validation.surname.notBlank}")
    private String surname;

    @NotNull(message = "{validation.age.notNull}")
    @Min(value = 16, message = "{validation.age.min}")
    @Max(value = 100, message = "{validation.age.max}")
    private Integer age;

    @NotBlank(message = "{validation.email.notBlank}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.password.notBlank}")
    @Size(min = 4, max = 24, message = "{validation.password.size}")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).+$",
            message = "{validation.password.pattern}"
    )
    private String password;

    @NotBlank(message = "{validation.phone.notBlank}")
    @Pattern(
            regexp = "^\\+?\\d{10,15}$",
            message = "{validation.phone.pattern}"
    )
    private String phoneNumber;

    @NotNull(message = "{validation.accountType.notNull}")
    private AccountType accountType;
}