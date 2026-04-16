package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.RespondedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Integer> {
}