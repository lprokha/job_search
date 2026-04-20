package kg.attractor.job_search.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Double salary;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "resume")
    private List<ContactInfo> contacts;

    @OneToMany(mappedBy = "resume")
    private List<EducationInfo> educationInfoList;

    @OneToMany(mappedBy = "resume")
    private List<WorkExperienceInfo> workExperienceInfoList;

    @OneToMany(mappedBy = "resume")
    private List<RespondedApplicant> respondedApplicants;

    public Integer getApplicantId() {
        return applicant != null ? applicant.getId() : null;
    }

    public void setApplicantId(Integer applicantId) {
        if (applicantId == null) {
            this.applicant = null;
            return;
        }
        User user = new User();
        user.setId(applicantId);
        this.applicant = user;
    }

    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }

    public void setCategoryId(Integer categoryId) {
        if (categoryId == null) {
            this.category = null;
            return;
        }
        Category category = new Category();
        category.setId(categoryId);
        this.category = category;
    }
}