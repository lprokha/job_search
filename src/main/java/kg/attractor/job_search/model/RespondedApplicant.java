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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "responded_applicants")
public class RespondedApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;

    @Column(name = "confirmation")
    private Boolean confirmation;

    @OneToMany(mappedBy = "respondedApplicant")
    private List<Message> messages;

    public Integer getResumeId() {
        return resume != null ? resume.getId() : null;
    }

    public void setResumeId(Integer resumeId) {
        if (resumeId == null) {
            this.resume = null;
            return;
        }
        Resume resume = new Resume();
        resume.setId(resumeId);
        this.resume = resume;
    }

    public Integer getVacancyId() {
        return vacancy != null ? vacancy.getId() : null;
    }

    public void setVacancyId(Integer vacancyId) {
        if (vacancyId == null) {
            this.vacancy = null;
            return;
        }
        Vacancy vacancy = new Vacancy();
        vacancy.setId(vacancyId);
        this.vacancy = vacancy;
    }
}