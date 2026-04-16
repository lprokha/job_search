package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.WorkExperienceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkExperienceInfoRepository extends JpaRepository<WorkExperienceInfo, Integer> {
}