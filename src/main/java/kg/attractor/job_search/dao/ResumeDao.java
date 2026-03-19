package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.Resume;
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
public class ResumeDao {
    private final JdbcTemplate jdbcTemplate;

    public Resume save(Resume resume) {
        String sql = """
                INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, resume.getApplicantId());
            ps.setString(2, resume.getName());
            ps.setObject(3, resume.getCategoryId());
            ps.setObject(4, resume.getSalary());
            ps.setObject(5, resume.getIsActive());
            ps.setObject(6, resume.getCreatedDate());
            ps.setObject(7, resume.getUpdateTime());
            return ps;
        }, keyHolder);

        resume.setId(keyHolder.getKey().intValue());
        return resume;
    }

    public Optional<Resume> findById(Integer id) {
        String sql = """
                SELECT id,
                       applicant_id AS applicantId,
                       name,
                       category_id AS categoryId,
                       salary,
                       is_active AS isActive,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM resumes
                WHERE id = ?
                """;
        List<Resume> resumes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Resume.class), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(resumes));
    }

    public List<Resume> findAll() {
        String sql = """
                SELECT id,
                       applicant_id AS applicantId,
                       name,
                       category_id AS categoryId,
                       salary,
                       is_active AS isActive,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM resumes
                ORDER BY update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Resume.class));
    }

    public List<Resume> findByCategory(Integer categoryId) {
        String sql = """
                SELECT id,
                       applicant_id AS applicantId,
                       name,
                       category_id AS categoryId,
                       salary,
                       is_active AS isActive,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM resumes
                WHERE category_id = ?
                ORDER BY update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Resume.class), categoryId);
    }

    public List<Resume> findByApplicantId(Integer applicantId) {
        String sql = """
                SELECT id,
                       applicant_id AS applicantId,
                       name,
                       category_id AS categoryId,
                       salary,
                       is_active AS isActive,
                       created_date AS createdDate,
                       update_time AS updateTime
                FROM resumes
                WHERE applicant_id = ?
                ORDER BY update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Resume.class), applicantId);
    }

    public List<Resume> findApplicantsByVacancyId(Integer vacancyId) {
        String sql = """
                SELECT r.id,
                       r.applicant_id AS applicantId,
                       r.name,
                       r.category_id AS categoryId,
                       r.salary,
                       r.is_active AS isActive,
                       r.created_date AS createdDate,
                       r.update_time AS updateTime
                FROM resumes r
                JOIN responded_applicants ra ON r.id = ra.resume_id
                WHERE ra.vacancy_id = ?
                ORDER BY r.update_time DESC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Resume.class), vacancyId);
    }

    public Optional<Resume> update(Resume resume) {
        String sql = """
                UPDATE resumes
                SET applicant_id = ?,
                    name = ?,
                    category_id = ?,
                    salary = ?,
                    is_active = ?,
                    update_time = ?
                WHERE id = ?
                """;

        int updated = jdbcTemplate.update(sql,
                resume.getApplicantId(),
                resume.getName(),
                resume.getCategoryId(),
                resume.getSalary(),
                resume.getIsActive(),
                resume.getUpdateTime(),
                resume.getId()
        );

        if (updated == 0) {
            return Optional.empty();
        }

        return findById(resume.getId());
    }

    public boolean delete(Integer id) {
        String sql = "DELETE FROM resumes WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}