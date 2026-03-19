package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(CreateUserDto dto);

    Optional<User> getById(Integer id);

    Optional<User> findApplicant(Integer id);

    Optional<User> findEmployer(Integer id);

    List<User> getAll();

    List<User> findByName(String name);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> updateAvatar(Integer userId, String avatarFileName);
}