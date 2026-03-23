package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.dto.UpdateUserDto;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User create(CreateUserDto dto) {
        log.info("Creating user with email={}", dto.getEmail());

        User user = new User(
                null,
                dto.getName(),
                dto.getSurname(),
                dto.getAge(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getPhoneNumber(),
                "default-avatar.png",
                dto.getAccountType()
        );

        User savedUser = userDao.save(user);
        log.debug("User created successfully with id={}", savedUser.getId());

        return savedUser;
    }

    @Override
    public Optional<User> updateProfile(Integer id, UpdateUserDto dto) {
        log.info("Updating profile for user id={}", id);

        Optional<User> existingUser = userDao.findById(id);
        if (existingUser.isEmpty()) {
            log.warn("User not found for profile update, id={}", id);
            return Optional.empty();
        }

        User user = existingUser.get();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());

        Optional<User> updatedUser = userDao.updateProfile(id, user);
        log.debug("Profile updated successfully for user id={}", id);

        return updatedUser;
    }

    @Override
    public Optional<User> getById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findApplicant(Integer id) {
        return userDao.findById(id)
                .filter(user -> user.getAccountType() == AccountType.APPLICANT);
    }

    @Override
    public Optional<User> findEmployer(Integer id) {
        return userDao.findById(id)
                .filter(user -> user.getAccountType() == AccountType.EMPLOYER);
    }

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> findByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userDao.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public Optional<User> updateAvatar(Integer userId, String avatarFileName) {
        return userDao.updateAvatar(userId, avatarFileName);
    }
}