package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryDao {

    private final JdbcTemplate jdbcTemplate;

    public List<Category> findAll() {
        String sql = """
                SELECT id,
                       name,
                       parent_id AS parentId
                FROM categories
                ORDER BY name
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }
}