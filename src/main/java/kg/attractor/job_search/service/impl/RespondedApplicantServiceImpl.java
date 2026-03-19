package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.RespondedApplicantDao;
import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.service.RespondedApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RespondedApplicantServiceImpl implements RespondedApplicantService {
    private final RespondedApplicantDao respondedApplicantDao;

    @Override
    public boolean existsByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId) {
        return respondedApplicantDao.existsByResumeIdAndVacancyId(resumeId, vacancyId);
    }

    @Override
    public RespondedApplicant create(RespondToVacancyDto dto) {
        RespondedApplicant response = new RespondedApplicant(
                null,
                dto.getResumeId(),
                dto.getVacancyId(),
                false
        );
        return respondedApplicantDao.save(response);
    }

    @Override
    public List<RespondedApplicant> getByVacancyId(Integer vacancyId) {
        return respondedApplicantDao.findByVacancyId(vacancyId);
    }

    @Override
    public List<RespondedApplicant> getByResumeId(Integer resumeId) {
        return respondedApplicantDao.findByResumeId(resumeId);
    }

    @Override
    public List<RespondedApplicant> getAll() {
        return respondedApplicantDao.findAll();
    }

    @Override
    public Optional<RespondedApplicant> getById(Integer id) {
        return respondedApplicantDao.findById(id);
    }
}