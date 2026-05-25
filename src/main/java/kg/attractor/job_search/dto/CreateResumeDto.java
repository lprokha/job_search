package kg.attractor.job_search.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private Integer contactTypeId;
    private String contactValue;

    @Valid
    @Builder.Default
    private List<ContactDto> contactInfos = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<EducationDto> educationInfos = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<WorkExperienceDto> workExperienceInfos = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactDto {
        private Integer typeId;
        private String contactValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationDto {
        private String institution;
        private String program;
        private String startDate;
        private String endDate;
        private String degree;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkExperienceDto {
        private Integer years;
        private String companyName;
        private String position;
        private String responsibilities;
    }
}