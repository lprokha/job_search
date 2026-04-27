package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByNameContainingIgnoreCase(String name);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);
}