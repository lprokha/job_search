package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.model.Resume;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ResumeService {
    Resume create(CreateResumeDto dto, Integer applicantId);

    Optional<Resume> getById(Integer id);

    List<Resume> getAll();

    Page<Resume> getAll(int page, int size);

    Page<Resume> getByApplicantId(Integer applicantId, int page, int size);

    Page<Resume> getByCategory(Integer categoryId, int page, int size);

    List<Resume> getApplicantsByVacancyId(Integer vacancyId);

    Optional<Resume> update(Integer id, UpdateResumeDto dto);

    boolean delete(Integer id);
}