package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.RespondedApplicant;
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
public class RespondedApplicantDao {
    private final JdbcTemplate jdbcTemplate;

    public boolean existsByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId) {
        String sql = """
                SELECT COUNT(*)
                FROM responded_applicants
                WHERE resume_id = ? AND vacancy_id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, resumeId, vacancyId);
        return count != null && count > 0;
    }

    public RespondedApplicant save(RespondedApplicant response) {
        String sql = """
                INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
                VALUES (?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, response.getResumeId());
            ps.setObject(2, response.getVacancyId());
            ps.setObject(3, response.getConfirmation());
            return ps;
        }, keyHolder);

        response.setId(keyHolder.getKey().intValue());
        return response;
    }

    public List<RespondedApplicant> findByVacancyId(Integer vacancyId) {
        String sql = """
                SELECT id,
                       resume_id AS resumeId,
                       vacancy_id AS vacancyId,
                       confirmation
                FROM responded_applicants
                WHERE vacancy_id = ?
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RespondedApplicant.class), vacancyId);
    }

    public List<RespondedApplicant> findByResumeId(Integer resumeId) {
        String sql = """
                SELECT id,
                       resume_id AS resumeId,
                       vacancy_id AS vacancyId,
                       confirmation
                FROM responded_applicants
                WHERE resume_id = ?
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RespondedApplicant.class), resumeId);
    }

    public List<RespondedApplicant> findAll() {
        String sql = """
                SELECT id,
                       resume_id AS resumeId,
                       vacancy_id AS vacancyId,
                       confirmation
                FROM responded_applicants
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RespondedApplicant.class));
    }

    public Optional<RespondedApplicant> findById(Integer id) {
        String sql = """
                SELECT id,
                       resume_id AS resumeId,
                       vacancy_id AS vacancyId,
                       confirmation
                FROM responded_applicants
                WHERE id = ?
                """;
        List<RespondedApplicant> responses = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RespondedApplicant.class), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(responses));
    }
}