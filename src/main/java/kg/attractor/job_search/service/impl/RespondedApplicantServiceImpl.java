package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.repository.RespondedApplicantRepository;
import kg.attractor.job_search.repository.ResumeRepository;
import kg.attractor.job_search.repository.VacancyRepository;
import kg.attractor.job_search.service.RespondedApplicantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RespondedApplicantServiceImpl implements RespondedApplicantService {
    private final RespondedApplicantRepository respondedApplicantRepository;
    private final ResumeRepository resumeRepository;
    private final VacancyRepository vacancyRepository;

    @Override
    public boolean existsByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId) {
        return respondedApplicantRepository.existsByResumeIdAndVacancyId(resumeId, vacancyId);
    }

    @Override
    public RespondedApplicant create(RespondToVacancyDto dto) {
        log.info("Creating response for resumeId={} and vacancyId={}", dto.getResumeId(), dto.getVacancyId());

        Resume resume = resumeRepository.findById(dto.getResumeId()).orElse(null);
        Vacancy vacancy = vacancyRepository.findById(dto.getVacancyId()).orElse(null);

        RespondedApplicant response = new RespondedApplicant();
        response.setResume(resume);
        response.setVacancy(vacancy);
        response.setConfirmation(false);

        RespondedApplicant savedResponse = respondedApplicantRepository.save(response);
        log.debug("Response created successfully with id={}", savedResponse.getId());

        return savedResponse;
    }

    @Override
    public List<RespondedApplicant> getByVacancyId(Integer vacancyId) {
        return respondedApplicantRepository.findByVacancyId(vacancyId);
    }

    @Override
    public List<RespondedApplicant> getByResumeId(Integer resumeId) {
        return respondedApplicantRepository.findByResumeId(resumeId);
    }

    @Override
    public List<RespondedApplicant> getAll() {
        return respondedApplicantRepository.findAll();
    }

    @Override
    public Optional<RespondedApplicant> getById(Integer id) {
        return respondedApplicantRepository.findById(id);
    }
}