package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, RoleAuthority.RoleAuthorityId> {
}