package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.model.Category;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.repository.CategoryRepository;
import kg.attractor.job_search.repository.ResumeRepository;
import kg.attractor.job_search.repository.UserRepository;
import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Resume create(CreateResumeDto dto, Integer applicantId) {
        log.info("Creating resume for applicantId={}", applicantId);

        LocalDateTime now = LocalDateTime.now();

        User applicant = userRepository.findById(applicantId).orElse(null);
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);

        Resume resume = Resume.builder()
                .applicant(applicant)
                .name(dto.getName())
                .category(category)
                .salary(dto.getSalary())
                .isActive(dto.getIsActive())
                .createdDate(now)
                .updateTime(now)
                .build();

        Resume savedResume = resumeRepository.save(resume);
        log.debug("Resume created successfully with id={}", savedResume.getId());

        return savedResume;
    }

    @Override
    public Optional<Resume> getById(Integer id) {
        return resumeRepository.findById(id);
    }

    @Override
    public List<Resume> getAll() {
        return resumeRepository.findAll();
    }

    @Override
    public List<Resume> getByCategory(Integer categoryId) {
        return resumeRepository.findByCategory_Id(categoryId);
    }

    @Override
    public List<Resume> getByApplicantId(Integer applicantId) {
        return resumeRepository.findByApplicant_Id(applicantId);
    }

    @Override
    public List<Resume> getApplicantsByVacancyId(Integer vacancyId) {
        return resumeRepository.findApplicantsByVacancyId(vacancyId);
    }

    @Override
    public Optional<Resume> update(Integer id, UpdateResumeDto dto) {
        log.info("Updating resume id={}", id);

        Optional<Resume> existingResume = resumeRepository.findById(id);
        if (existingResume.isEmpty()) {
            log.warn("Resume not found for update, id={}", id);
            return Optional.empty();
        }

        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);

        Resume resume = existingResume.get();
        resume.setName(dto.getName());
        resume.setCategory(category);
        resume.setSalary(dto.getSalary());
        resume.setIsActive(dto.getIsActive());
        resume.setUpdateTime(LocalDateTime.now());

        Resume updatedResume = resumeRepository.save(resume);
        log.debug("Resume updated successfully, id={}", id);

        return Optional.of(updatedResume);
    }

    @Override
    public boolean delete(Integer id) {
        log.warn("Deleting resume id={}", id);

        if (!resumeRepository.existsById(id)) {
            return false;
        }

        resumeRepository.deleteById(id);
        return true;
    }
}