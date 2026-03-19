package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.model.Vacancy;

import java.util.List;
import java.util.Optional;

public interface VacancyService {
    Vacancy create(CreateVacancyDto dto);

    Optional<Vacancy> getById(Integer id);

    List<Vacancy> getAll();

    List<Vacancy> getAllActive();

    List<Vacancy> getActiveByCategory(Integer categoryId);

    List<Vacancy> getByAuthorId(Integer authorId);

    List<Vacancy> getRespondedVacanciesByApplicantId(Integer applicantId);

    Optional<Vacancy> update(Integer id, UpdateVacancyDto dto);

    boolean delete(Integer id);
}