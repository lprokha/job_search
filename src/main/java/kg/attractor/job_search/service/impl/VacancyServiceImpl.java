package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;

    @Override
    public Vacancy create(CreateVacancyDto dto) {
        LocalDateTime now = LocalDateTime.now();

        Vacancy vacancy = new Vacancy(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getCategoryId(),
                dto.getSalary(),
                dto.getExpFrom(),
                dto.getExpTo(),
                dto.getIsActive(),
                dto.getAuthorId(),
                now,
                now
        );

        return vacancyDao.save(vacancy);
    }

    @Override
    public Optional<Vacancy> getById(Integer id) {
        return vacancyDao.findById(id);
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyDao.findAll();
    }

    @Override
    public List<Vacancy> getAllActive() {
        return vacancyDao.findAllActive();
    }

    @Override
    public List<Vacancy> getActiveByCategory(Integer categoryId) {
        return vacancyDao.findActiveByCategory(categoryId);
    }

    @Override
    public List<Vacancy> getByAuthorId(Integer authorId) {
        return vacancyDao.findByAuthorId(authorId);
    }

    @Override
    public List<Vacancy> getRespondedVacanciesByApplicantId(Integer applicantId) {
        return vacancyDao.findRespondedByApplicantId(applicantId);
    }

    @Override
    public Optional<Vacancy> update(Integer id, UpdateVacancyDto dto) {
        Optional<Vacancy> existingVacancy = vacancyDao.findById(id);
        if (existingVacancy.isEmpty()) {
            return Optional.empty();
        }

        Vacancy vacancy = existingVacancy.get();
        vacancy.setAuthorId(dto.getAuthorId());
        vacancy.setName(dto.getName());
        vacancy.setDescription(dto.getDescription());
        vacancy.setCategoryId(dto.getCategoryId());
        vacancy.setSalary(dto.getSalary());
        vacancy.setExpFrom(dto.getExpFrom());
        vacancy.setExpTo(dto.getExpTo());
        vacancy.setIsActive(dto.getIsActive());
        vacancy.setUpdateTime(LocalDateTime.now());

        return vacancyDao.update(vacancy);
    }

    @Override
    public boolean delete(Integer id) {
        return vacancyDao.delete(id);
    }
}