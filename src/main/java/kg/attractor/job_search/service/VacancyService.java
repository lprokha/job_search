package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.model.Vacancy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VacancyService {
    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    public Vacancy create(CreateVacancyDto dto) {
        LocalDateTime now = LocalDateTime.now();

        Vacancy vacancy = new Vacancy(
                idGenerator.incrementAndGet(),
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

        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    public Optional<Vacancy> getById(Integer id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    public List<Vacancy> getAllActive() {
        return vacancies.values()
                .stream()
                .filter(Vacancy::getIsActive)
                .sorted(Comparator.comparing(Vacancy::getUpdateTime).reversed())
                .toList();
    }

    public List<Vacancy> getActiveByCategory(Integer categoryId) {
        return vacancies.values()
                .stream()
                .filter(Vacancy::getIsActive)
                .filter(vacancy -> vacancy.getCategoryId().equals(categoryId))
                .sorted(Comparator.comparing(Vacancy::getUpdateTime).reversed())
                .toList();
    }

    public Optional<Vacancy> update(Integer id, UpdateVacancyDto dto) {
        Vacancy vacancy = vacancies.get(id);
        if (vacancy == null) {
            return Optional.empty();
        }

        vacancy.setAuthorId(dto.getAuthorId());
        vacancy.setName(dto.getName());
        vacancy.setDescription(dto.getDescription());
        vacancy.setCategoryId(dto.getCategoryId());
        vacancy.setSalary(dto.getSalary());
        vacancy.setExpFrom(dto.getExpFrom());
        vacancy.setExpTo(dto.getExpTo());
        vacancy.setIsActive(dto.getIsActive());
        vacancy.setUpdateTime(LocalDateTime.now());

        return Optional.of(vacancy);
    }

    public boolean delete(Integer id) {
        return vacancies.remove(id) != null;
    }
}