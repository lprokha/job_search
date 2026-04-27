package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Role;
import kg.attractor.job_search.model.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, RoleAuthority.RoleAuthorityId> {

    List<RoleAuthority> findById_Role(Role role);
}