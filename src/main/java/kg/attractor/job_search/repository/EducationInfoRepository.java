package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationInfoRepository extends JpaRepository<EducationInfo, Integer> {
}