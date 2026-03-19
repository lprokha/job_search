package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public User create(CreateUserDto dto) {
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
        return userDao.save(user);
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