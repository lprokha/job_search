package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactTypeRepository extends JpaRepository<ContactType, Integer> {
}