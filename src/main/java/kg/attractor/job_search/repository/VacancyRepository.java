package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Integer> {

    Page<Vacancy> findByIsActiveTrue(Pageable pageable);

    Page<Vacancy> findByIsActiveTrueAndCategory_Id(Integer categoryId, Pageable pageable);

    Page<Vacancy> findByAuthor_Id(Integer authorId, Pageable pageable);

    @Query("""
            select v
            from Vacancy v
            join RespondedApplicant ra on ra.vacancy.id = v.id
            where ra.resume.applicant.id = :applicantId
            """)
    List<Vacancy> findRespondedByApplicantId(Integer applicantId);
}