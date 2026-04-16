package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}