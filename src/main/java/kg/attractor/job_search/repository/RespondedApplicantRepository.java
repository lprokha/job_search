package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.RespondedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Integer> {

    boolean existsByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId);

    List<RespondedApplicant> findByVacancyId(Integer vacancyId);

    List<RespondedApplicant> findByResumeId(Integer resumeId);
}