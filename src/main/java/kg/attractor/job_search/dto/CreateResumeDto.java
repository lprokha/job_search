package kg.attractor.job_search.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "{validation.resume.name.notBlank}")
    private String name;

    @NotNull(message = "{validation.category.notNull}")
    private Integer categoryId;

    @NotNull(message = "{validation.salary.notNull}")
    @Positive(message = "{validation.salary.positive}")
    private Double salary;

    private Boolean isActive;

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
        @NotNull(message = "{validation.contact.type.notNull}")
        private Integer typeId;

        @NotBlank(message = "{validation.contact.value.notBlank}")
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
        @Positive(message = "{validation.resume.workYears.positive}")
        private Integer years;

        private String companyName;
        private String position;
        private String responsibilities;
    }
}