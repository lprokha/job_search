package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.model.RespondedApplicant;

import java.util.List;
import java.util.Optional;

public interface RespondedApplicantService {
    boolean existsByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId);

    RespondedApplicant create(RespondToVacancyDto dto);

    List<RespondedApplicant> getByVacancyId(Integer vacancyId);

    List<RespondedApplicant> getByResumeId(Integer resumeId);

    List<RespondedApplicant> getAll();

    Optional<RespondedApplicant> getById(Integer id);
}