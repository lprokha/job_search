package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Integer> {
}