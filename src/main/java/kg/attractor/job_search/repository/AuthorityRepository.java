package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}