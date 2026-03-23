package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.model.Resume;
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
    private final ResumeDao resumeDao;

    @Override
    public Resume create(CreateResumeDto dto) {
        log.info("Creating resume for applicantId={}", dto.getApplicantId());

        LocalDateTime now = LocalDateTime.now();

        Resume resume = new Resume(
                null,
                dto.getApplicantId(),
                dto.getName(),
                dto.getCategoryId(),
                dto.getSalary(),
                dto.getIsActive(),
                now,
                now
        );

        Resume savedResume = resumeDao.save(resume);
        log.debug("Resume created successfully with id={}", savedResume.getId());

        return savedResume;
    }

    @Override
    public Optional<Resume> getById(Integer id) {
        return resumeDao.findById(id);
    }

    @Override
    public List<Resume> getAll() {
        return resumeDao.findAll();
    }

    @Override
    public List<Resume> getByCategory(Integer categoryId) {
        return resumeDao.findByCategory(categoryId);
    }

    @Override
    public List<Resume> getByApplicantId(Integer applicantId) {
        return resumeDao.findByApplicantId(applicantId);
    }

    @Override
    public List<Resume> getApplicantsByVacancyId(Integer vacancyId) {
        return resumeDao.findApplicantsByVacancyId(vacancyId);
    }

    @Override
    public Optional<Resume> update(Integer id, UpdateResumeDto dto) {
        log.info("Updating resume id={}", id);

        Optional<Resume> existingResume = resumeDao.findById(id);
        if (existingResume.isEmpty()) {
            log.warn("Resume not found for update, id={}", id);
            return Optional.empty();
        }

        Resume resume = existingResume.get();
        resume.setApplicantId(dto.getApplicantId());
        resume.setName(dto.getName());
        resume.setCategoryId(dto.getCategoryId());
        resume.setSalary(dto.getSalary());
        resume.setIsActive(dto.getIsActive());
        resume.setUpdateTime(LocalDateTime.now());

        Optional<Resume> updatedResume = resumeDao.update(resume);
        log.debug("Resume updated successfully, id={}", id);

        return updatedResume;
    }

    @Override
    public boolean delete(Integer id) {
        log.warn("Deleting resume id={}", id);
        return resumeDao.delete(id);
    }
}