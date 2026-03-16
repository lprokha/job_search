package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

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

    public Optional<User> getById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findApplicant(Integer id) {
        return getById(id).filter(user -> user.getAccountType() == AccountType.APPLICANT);
    }

    public Optional<User> findEmployer(Integer id) {
        return getById(id).filter(user -> user.getAccountType() == AccountType.EMPLOYER);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User updateAvatar(Integer userId, String avatarFileName) {
        User user = users.get(userId);
        if (user == null) {
            return null;
        }
        user.setAvatar(avatarFileName);
        return user;
    }
}