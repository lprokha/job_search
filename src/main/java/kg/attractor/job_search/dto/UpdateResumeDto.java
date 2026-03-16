package kg.attractor.job_search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResumeDto {
    private Integer applicantId;
    private String name;
    private Integer categoryId;
    private Double salary;
    private Boolean isActive;
}