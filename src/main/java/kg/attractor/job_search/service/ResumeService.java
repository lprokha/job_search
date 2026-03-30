package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.model.Resume;

import java.util.List;
import java.util.Optional;

public interface ResumeService {
    Resume create(CreateResumeDto dto, Integer applicantId);

    Optional<Resume> getById(Integer id);

    List<Resume> getAll();

    List<Resume> getByCategory(Integer categoryId);

    List<Resume> getByApplicantId(Integer applicantId);

    List<Resume> getApplicantsByVacancyId(Integer vacancyId);

    Optional<Resume> update(Integer id, UpdateResumeDto dto);

    boolean delete(Integer id);
}