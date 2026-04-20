package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    List<Resume> findByCategory_Id(Integer categoryId);

    List<Resume> findByApplicant_Id(Integer applicantId);

    Page<Resume> findByCategory_Id(Integer categoryId, Pageable pageable);

    Page<Resume> findByApplicant_Id(Integer applicantId, Pageable pageable);

    Page<Resume> findAll(Pageable pageable);

    @Query("""
            select r
            from Resume r
            join RespondedApplicant ra on ra.resume.id = r.id
            where ra.vacancy.id = :vacancyId
            """)
    List<Resume> findApplicantsByVacancyId(Integer vacancyId);
}