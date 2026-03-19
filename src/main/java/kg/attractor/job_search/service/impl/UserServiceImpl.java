package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public User create(CreateUserDto dto) {
        User user = new User(
                idGenerator.incrementAndGet(),
                dto.getName(),
                dto.getSurname(),
                dto.getAge(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getPhoneNumber(),
                "default-avatar.png",
                dto.getAccountType()
        );

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findApplicant(Integer id) {
        return getById(id).filter(user -> user.getAccountType() == AccountType.APPLICANT);
    }

    @Override
    public Optional<User> findEmployer(Integer id) {
        return getById(id).filter(user -> user.getAccountType() == AccountType.EMPLOYER);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> findByName(String name) {
        String searchValue = name == null ? "" : name.toLowerCase();

        return users.values().stream()
                .filter(user -> user.getName() != null && user.getName().toLowerCase().contains(searchValue))
                .toList();
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return users.values().stream()
                .filter(user -> user.getPhoneNumber() != null && user.getPhoneNumber().equals(phoneNumber))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail() != null && user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail() != null && user.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public Optional<User> updateAvatar(Integer userId, String avatarFileName) {
        User user = users.get(userId);
        if (user == null) {
            return Optional.empty();
        }

        user.setAvatar(avatarFileName);
        return Optional.of(user);
    }
}