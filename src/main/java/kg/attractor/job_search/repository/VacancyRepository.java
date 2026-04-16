package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Integer> {

    List<Vacancy> findByIsActiveTrue();

    List<Vacancy> findByIsActiveTrueAndCategoryId(Integer categoryId);

    List<Vacancy> findByAuthorId(Integer authorId);

    @Query("""
            select v
            from Vacancy v
            join RespondedApplicant ra on ra.vacancy.id = v.id
            where ra.resume.applicant.id = :applicantId
            """)
    List<Vacancy> findRespondedByApplicantId(Integer applicantId);
}