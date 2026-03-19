package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;

    @Override
    public Resume create(CreateResumeDto dto) {
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

        return resumeDao.save(resume);
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

    public List<Resume> getApplicantsByVacancyId(Integer vacancyId) {
        return resumeDao.findApplicantsByVacancyId(vacancyId);
    }

    @Override
    public Optional<Resume> update(Integer id, UpdateResumeDto dto) {
        Optional<Resume> existingResume = resumeDao.findById(id);
        if (existingResume.isEmpty()) {
            return Optional.empty();
        }

        Resume resume = existingResume.get();
        resume.setApplicantId(dto.getApplicantId());
        resume.setName(dto.getName());
        resume.setCategoryId(dto.getCategoryId());
        resume.setSalary(dto.getSalary());
        resume.setIsActive(dto.getIsActive());
        resume.setUpdateTime(LocalDateTime.now());

        return resumeDao.update(resume);
    }

    @Override
    public boolean delete(Integer id) {
        return resumeDao.delete(id);
    }
}