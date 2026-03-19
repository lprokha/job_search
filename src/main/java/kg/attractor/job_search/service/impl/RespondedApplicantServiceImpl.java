package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.service.RespondedApplicantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RespondedApplicantServiceImpl implements RespondedApplicantService {
    private final Map<Integer, RespondedApplicant> responses = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public boolean existsByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId) {
        return responses.values().stream()
                .anyMatch(response ->
                        response.getResumeId() != null
                                && response.getVacancyId() != null
                                && response.getResumeId().equals(resumeId)
                                && response.getVacancyId().equals(vacancyId)
                );
    }

    @Override
    public RespondedApplicant create(RespondToVacancyDto dto) {
        RespondedApplicant response = new RespondedApplicant(
                idGenerator.incrementAndGet(),
                dto.getResumeId(),
                dto.getVacancyId(),
                false
        );

        responses.put(response.getId(), response);
        return response;
    }

    @Override
    public List<RespondedApplicant> getByVacancyId(Integer vacancyId) {
        return responses.values().stream()
                .filter(response -> response.getVacancyId() != null && response.getVacancyId().equals(vacancyId))
                .toList();
    }

    @Override
    public List<RespondedApplicant> getByResumeId(Integer resumeId) {
        return responses.values().stream()
                .filter(response -> response.getResumeId() != null && response.getResumeId().equals(resumeId))
                .toList();
    }

    @Override
    public List<RespondedApplicant> getAll() {
        return responses.values().stream().toList();
    }

    @Override
    public Optional<RespondedApplicant> getById(Integer id) {
        return Optional.ofNullable(responses.get(id));
    }
}