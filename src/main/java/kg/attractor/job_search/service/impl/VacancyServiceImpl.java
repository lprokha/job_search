package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.model.Category;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.repository.CategoryRepository;
import kg.attractor.job_search.repository.UserRepository;
import kg.attractor.job_search.repository.VacancyRepository;
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
    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Vacancy create(CreateVacancyDto dto, Integer authorId) {
        log.info("Creating vacancy for authorId={}", authorId);

        LocalDateTime now = LocalDateTime.now();

        User author = userRepository.findById(authorId).orElse(null);
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);

        Vacancy vacancy = Vacancy.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .category(category)
                .salary(dto.getSalary())
                .expFrom(dto.getExpFrom())
                .expTo(dto.getExpTo())
                .isActive(dto.getIsActive())
                .author(author)
                .createdDate(now)
                .updateTime(now)
                .build();

        Vacancy savedVacancy = vacancyRepository.save(vacancy);
        log.debug("Vacancy created successfully with id={}", savedVacancy.getId());

        return savedVacancy;
    }

    @Override
    public Optional<Vacancy> getById(Integer id) {
        return vacancyRepository.findById(id);
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<Vacancy> getAllActive() {
        return vacancyRepository.findByIsActiveTrue();
    }

    @Override
    public List<Vacancy> getActiveByCategory(Integer categoryId) {
        return vacancyRepository.findByIsActiveTrueAndCategory_Id(categoryId);    }

    @Override
    public List<Vacancy> getByAuthorId(Integer authorId) {
        return vacancyRepository.findByAuthor_Id(authorId);    }

    @Override
    public List<Vacancy> getRespondedVacanciesByApplicantId(Integer applicantId) {
        return vacancyRepository.findRespondedByApplicantId(applicantId);
    }

    @Override
    public Optional<Vacancy> update(Integer id, UpdateVacancyDto dto) {
        log.info("Updating vacancy id={}", id);

        Optional<Vacancy> existingVacancy = vacancyRepository.findById(id);
        if (existingVacancy.isEmpty()) {
            log.warn("Vacancy not found for update, id={}", id);
            return Optional.empty();
        }

        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);

        Vacancy vacancy = existingVacancy.get();
        vacancy.setName(dto.getName());
        vacancy.setDescription(dto.getDescription());
        vacancy.setCategory(category);
        vacancy.setSalary(dto.getSalary());
        vacancy.setExpFrom(dto.getExpFrom());
        vacancy.setExpTo(dto.getExpTo());
        vacancy.setIsActive(dto.getIsActive());
        vacancy.setUpdateTime(LocalDateTime.now());

        Vacancy updatedVacancy = vacancyRepository.save(vacancy);
        log.debug("Vacancy updated successfully, id={}", id);

        return Optional.of(updatedVacancy);
    }

    @Override
    public boolean delete(Integer id) {
        log.warn("Deleting vacancy id={}", id);

        if (!vacancyRepository.existsById(id)) {
            return false;
        }

        vacancyRepository.deleteById(id);
        return true;
    }
}