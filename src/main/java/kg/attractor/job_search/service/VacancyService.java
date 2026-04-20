package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.model.Vacancy;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface VacancyService {
    Vacancy create(CreateVacancyDto dto, Integer authorId);

    Optional<Vacancy> getById(Integer id);

    List<Vacancy> getAll();

    List<Vacancy> getAllActive();

    List<Vacancy> getActiveByCategory(Integer categoryId);

    List<Vacancy> getByAuthorId(Integer authorId);

    Page<Vacancy> getAllActive(int page, int size, String sortBy);

    Page<Vacancy> getActiveByCategory(Integer categoryId, int page, int size, String sortBy);

    Page<Vacancy> getByAuthorId(Integer authorId, int page, int size, String sortBy);

    List<Vacancy> getRespondedVacanciesByApplicantId(Integer applicantId);

    Optional<Vacancy> update(Integer id, UpdateVacancyDto dto);

    boolean delete(Integer id);
}