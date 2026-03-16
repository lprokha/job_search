package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.model.Resume;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ResumeService {
    private final Map<Integer, Resume> resumes = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    public Resume create(CreateResumeDto dto) {
        LocalDateTime now = LocalDateTime.now();

        Resume resume = new Resume(
                idGenerator.incrementAndGet(),
                dto.getApplicantId(),
                dto.getName(),
                dto.getCategoryId(),
                dto.getSalary(),
                dto.getIsActive(),
                now,
                now
        );

        resumes.put(resume.getId(), resume);
        return resume;
    }

    public Optional<Resume> getById(Integer id) {
        return Optional.ofNullable(resumes.get(id));
    }

    public List<Resume> getAll() {
        return resumes.values()
                .stream()
                .sorted(Comparator.comparing(Resume::getUpdateTime).reversed())
                .toList();
    }

    public List<Resume> getByCategory(Integer categoryId) {
        return resumes.values()
                .stream()
                .filter(resume -> resume.getCategoryId().equals(categoryId))
                .sorted(Comparator.comparing(Resume::getUpdateTime).reversed())
                .toList();
    }

    public Optional<Resume> update(Integer id, UpdateResumeDto dto) {
        Resume resume = resumes.get(id);
        if (resume == null) {
            return Optional.empty();
        }

        resume.setApplicantId(dto.getApplicantId());
        resume.setName(dto.getName());
        resume.setCategoryId(dto.getCategoryId());
        resume.setSalary(dto.getSalary());
        resume.setIsActive(dto.getIsActive());
        resume.setUpdateTime(LocalDateTime.now());

        return Optional.of(resume);
    }

    public boolean delete(Integer id) {
        return resumes.remove(id) != null;
    }
}