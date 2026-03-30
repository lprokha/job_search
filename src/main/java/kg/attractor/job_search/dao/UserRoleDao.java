package kg.attractor.job_search.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRoleDao {

    private final JdbcTemplate jdbcTemplate;

    public void assignRole(Integer userId, String roleName) {
        String sql = """
                INSERT INTO user_role(user_id, role_id)
                VALUES (?, (SELECT id FROM roles WHERE role = ?))
                """;

        jdbcTemplate.update(sql, userId, roleName);
    }
}