package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    List<Resume> findByCategoryId(Integer categoryId);

    List<Resume> findByApplicantId(Integer applicantId);

    @Query("""
            select r
            from Resume r
            join RespondedApplicant ra on ra.resume.id = r.id
            where ra.vacancy.id = :vacancyId
            """)
    List<Resume> findApplicantsByVacancyId(Integer vacancyId);
}