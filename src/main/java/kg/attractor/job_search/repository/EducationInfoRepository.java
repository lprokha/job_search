package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationInfoRepository extends JpaRepository<EducationInfo, Integer> {

    List<EducationInfo> findByResumeId(Integer resumeId);

    void deleteByResumeId(Integer resumeId);
}