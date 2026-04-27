package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Integer> {

    List<ContactInfo> findByResumeId(Integer resumeId);

    void deleteByResumeId(Integer resumeId);
}