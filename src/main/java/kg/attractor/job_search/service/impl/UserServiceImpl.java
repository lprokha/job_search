package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.dto.UpdateUserDto;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.Role;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.UserRole;
import kg.attractor.job_search.repository.RoleRepository;
import kg.attractor.job_search.repository.UserRepository;
import kg.attractor.job_search.repository.UserRoleRepository;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(CreateUserDto dto) {
        log.info("Creating user with email={}", dto.getEmail());

        User user = User.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .age(dto.getAge())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber())
                .avatar("default-avatar.png")
                .accountType(dto.getAccountType())
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);
        log.debug("User created successfully with id={}", savedUser.getId());

        Role role = roleRepository.findByRoleName(dto.getAccountType().name()).orElse(null);
        if (role != null) {
            UserRole.UserRoleId userRoleId = new UserRole.UserRoleId(savedUser, role);
            UserRole userRole = new UserRole(userRoleId);
            userRoleRepository.save(userRole);
            log.debug("Role {} assigned to user id={}", dto.getAccountType().name(), savedUser.getId());
        }

        return savedUser;
    }

    @Override
    public Optional<User> updateProfile(Integer id, UpdateUserDto dto) {
        log.info("Updating profile for user id={}", id);

        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            log.warn("User not found for profile update, id={}", id);
            return Optional.empty();
        }

        User user = existingUser.get();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.debug("Profile updated successfully for user id={}", id);

        return Optional.of(updatedUser);
    }

    @Override
    public Optional<User> getById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findApplicant(Integer id) {
        return userRepository.findById(id)
                .filter(user -> user.getAccountType() == AccountType.APPLICANT);
    }

    @Override
    public Optional<User> findEmployer(Integer id) {
        return userRepository.findById(id)
                .filter(user -> user.getAccountType() == AccountType.EMPLOYER);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> updateAvatar(Integer userId, String avatarFileName) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }

        User user = existingUser.get();
        user.setAvatar(avatarFileName);

        User updatedUser = userRepository.save(user);
        return Optional.of(updatedUser);
    }
}