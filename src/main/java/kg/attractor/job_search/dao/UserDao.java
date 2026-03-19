package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public User save(User user) {
        String sql = """
                INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setObject(3, user.getAge());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getPhoneNumber());
            ps.setString(7, user.getAvatar());
            ps.setString(8, user.getAccountType().name());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    public Optional<User> findById(Integer id) {
        String sql = """
                SELECT id, name, surname, age, email, password,
                       phone_number AS phoneNumber,
                       avatar,
                       account_type AS accountType
                FROM users
                WHERE id = ?
                """;
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(users));
    }

    public List<User> findAll() {
        String sql = """
                SELECT id, name, surname, age, email, password,
                       phone_number AS phoneNumber,
                       avatar,
                       account_type AS accountType
                FROM users
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public List<User> findByName(String name) {
        String sql = """
                SELECT id, name, surname, age, email, password,
                       phone_number AS phoneNumber,
                       avatar,
                       account_type AS accountType
                FROM users
                WHERE LOWER(name) LIKE LOWER(?)
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), "%" + name + "%");
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        String sql = """
                SELECT id, name, surname, age, email, password,
                       phone_number AS phoneNumber,
                       avatar,
                       account_type AS accountType
                FROM users
                WHERE phone_number = ?
                """;
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), phoneNumber);
        return Optional.ofNullable(DataAccessUtils.singleResult(users));
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, name, surname, age, email, password,
                       phone_number AS phoneNumber,
                       avatar,
                       account_type AS accountType
                FROM users
                WHERE LOWER(email) = LOWER(?)
                """;
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        return Optional.ofNullable(DataAccessUtils.singleResult(users));
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE LOWER(email) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public Optional<User> updateAvatar(Integer userId, String avatarFileName) {
        String sql = "UPDATE users SET avatar = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql, avatarFileName, userId);
        if (updated == 0) {
            return Optional.empty();
        }
        return findById(userId);
    }
}