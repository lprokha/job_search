package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;

    @Override
    public Vacancy create(CreateVacancyDto dto) {
        log.info("Creating vacancy for authorId={}", dto.getAuthorId());

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

        Vacancy savedVacancy = vacancyDao.save(vacancy);
        log.debug("Vacancy created successfully with id={}", savedVacancy.getId());

        return savedVacancy;
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
        log.info("Updating vacancy id={}", id);

        Optional<Vacancy> existingVacancy = vacancyDao.findById(id);
        if (existingVacancy.isEmpty()) {
            log.warn("Vacancy not found for update, id={}", id);
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

        Optional<Vacancy> updatedVacancy = vacancyDao.update(vacancy);
        log.debug("Vacancy updated successfully, id={}", id);

        return updatedVacancy;
    }

    @Override
    public boolean delete(Integer id) {
        log.warn("Deleting vacancy id={}", id);
        return vacancyDao.delete(id);
    }
}