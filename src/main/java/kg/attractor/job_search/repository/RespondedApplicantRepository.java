package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.RespondedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Integer> {

    boolean existsByResume_IdAndVacancy_Id(Integer resumeId, Integer vacancyId);

    Optional<RespondedApplicant> findByResume_IdAndVacancy_Id(Integer resumeId, Integer vacancyId);

    List<RespondedApplicant> findByVacancy_Id(Integer vacancyId);

    List<RespondedApplicant> findByResume_Id(Integer resumeId);

    long countByResume_Applicant_Id(Integer applicantId);

    long countByVacancy_Author_Id(Integer employerId);

    long countByVacancy_Id(Integer vacancyId);
}