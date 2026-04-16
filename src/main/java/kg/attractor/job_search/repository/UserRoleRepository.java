package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
}