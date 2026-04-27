package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {

    List<UserRole> findById_User(User user);
}