package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {
}