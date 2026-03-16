package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.model.RespondedApplicant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RespondedApplicantService {
    private final Map<Integer, RespondedApplicant> responses = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    public boolean existsByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId) {
        return responses.values().stream()
                .anyMatch(response ->
                        response.getResumeId().equals(resumeId)
                                && response.getVacancyId().equals(vacancyId)
                );
    }

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

    public List<RespondedApplicant> getByVacancyId(Integer vacancyId) {
        return responses.values().stream()
                .filter(response -> response.getVacancyId().equals(vacancyId))
                .toList();
    }

    public Optional<RespondedApplicant> getById(Integer id) {
        return Optional.ofNullable(responses.get(id));
    }
}