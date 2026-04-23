package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {

    List<UserRole> findById_User(User user);
}