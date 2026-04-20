package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.WorkExperienceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkExperienceInfoRepository extends JpaRepository<WorkExperienceInfo, Integer> {

    List<WorkExperienceInfo> findByResumeId(Integer resumeId);

    void deleteByResumeId(Integer resumeId);
}