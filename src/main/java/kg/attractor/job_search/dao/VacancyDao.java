package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.Vacancy;
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
public class VacancyDao {
    private final JdbcTemplate jdbcTemplate;

    public Vacancy save(Vacancy vacancy) {
        String sql = """
                INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, vacancy.getName());
            ps.setString(2, vacancy.getDescription());
            ps.setObject(3, vacancy.getCategoryId());
            ps.setObject(4, vacancy.getSalary());
            ps.setObject(5, vacancy.getExpFrom());
            ps.setObject(6, vacancy.getExpTo());
            ps.setObject(7, vacancy.getIsActive());
            ps.setObject(8, vacancy.getAuthorId());
            ps.setObject(9, vacancy.getCreatedDate());
            ps.setObject(10, vacancy.getUpdateTime());
            return ps;
        }, keyHolder);

        vacancy.setId(keyHolder.getKey().intValue());
        return vacancy;
    }

    public Optional<Vacancy> findById(Integer id) {
        String sql = """
                SELECT id,
                       name,
                       description,
                       category_id AS categoryId,
                       salary,
                       exp_from AS expFrom,
                       exp_to AS expTo,
                       is_active AS isActive,
                       author_id AS authorId,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM vacancies
                WHERE id = ?
                """;
        List<Vacancy> vacancies = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(vacancies));
    }

    public List<Vacancy> findAll() {
        String sql = """
                SELECT id,
                       name,
                       description,
                       category_id AS categoryId,
                       salary,
                       exp_from AS expFrom,
                       exp_to AS expTo,
                       is_active AS isActive,
                       author_id AS authorId,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM vacancies
                ORDER BY update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Vacancy.class));
    }

    public List<Vacancy> findAllActive() {
        String sql = """
                SELECT id,
                       name,
                       description,
                       category_id AS categoryId,
                       salary,
                       exp_from AS expFrom,
                       exp_to AS expTo,
                       is_active AS isActive,
                       author_id AS authorId,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM vacancies
                WHERE is_active = TRUE
                ORDER BY update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Vacancy.class));
    }

    public List<Vacancy> findActiveByCategory(Integer categoryId) {
        String sql = """
                SELECT id,
                       name,
                       description,
                       category_id AS categoryId,
                       salary,
                       exp_from AS expFrom,
                       exp_to AS expTo,
                       is_active AS isActive,
                       author_id AS authorId,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM vacancies
                WHERE is_active = TRUE AND category_id = ?
                ORDER BY update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), categoryId);
    }

    public List<Vacancy> findByAuthorId(Integer authorId) {
        String sql = """
                SELECT id,
                       name,
                       description,
                       category_id AS categoryId,
                       salary,
                       exp_from AS expFrom,
                       exp_to AS expTo,
                       is_active AS isActive,
                       author_id AS authorId,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM vacancies
                WHERE author_id = ?
                ORDER BY update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), authorId);
    }

    public List<Vacancy> findRespondedByApplicantId(Integer applicantId) {
        String sql = """
                SELECT v.id,
                       v.name,
                       v.description,
                       v.category_id AS categoryId,
                       v.salary,
                       v.exp_from AS expFrom,
                       v.exp_to AS expTo,
                       v.is_active AS isActive,
                       v.author_id AS authorId,
                       v.created_date AS createdDate,
                       v.update_time AS updateTime
                FROM vacancies v
                JOIN responded_applicants ra ON v.id = ra.vacancy_id
                JOIN resumes r ON r.id = ra.resume_id
                WHERE r.applicant_id = ?
                ORDER BY v.update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), applicantId);
    }

    public Optional<Vacancy> update(Vacancy vacancy) {
        String sql = """
                UPDATE vacancies
                SET name = ?,
                    description = ?,
                    category_id = ?,
                    salary = ?,
                    exp_from = ?,
                    exp_to = ?,
                    is_active = ?,
                    author_id = ?,
                    update_time = ?
                WHERE id = ?
                """;

        int updated = jdbcTemplate.update(sql,
                vacancy.getName(),
                vacancy.getDescription(),
                vacancy.getCategoryId(),
                vacancy.getSalary(),
                vacancy.getExpFrom(),
                vacancy.getExpTo(),
                vacancy.getIsActive(),
                vacancy.getAuthorId(),
                vacancy.getUpdateTime(),
                vacancy.getId()
        );

        if (updated == 0) {
            return Optional.empty();
        }

        return findById(vacancy.getId());
    }

    public boolean delete(Integer id) {
        String sql = "DELETE FROM vacancies WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}