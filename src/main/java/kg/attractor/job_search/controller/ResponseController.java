package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.RespondedApplicantService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/responses")
@RequiredArgsConstructor
public class ResponseController {

    private final RespondedApplicantService respondedApplicantService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    @PostMapping
    public ResponseEntity<RespondedApplicant> respondToVacancy(@RequestBody RespondToVacancyDto dto) {
        if (resumeService.getById(dto.getResumeId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Vacancy vacancy = vacancyService.getById(dto.getVacancyId()).orElse(null);
        if (vacancy == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!Boolean.TRUE.equals(vacancy.getIsActive())) {
            return ResponseEntity.badRequest().build();
        }

        if (respondedApplicantService.existsByResumeIdAndVacancyId(dto.getResumeId(), dto.getVacancyId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        RespondedApplicant createdResponse = respondedApplicantService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResponse);
    }

    @GetMapping("/vacancy/{vacancyId}/applicants")
    public ResponseEntity<List<Resume>> getApplicantsByVacancy(@PathVariable Integer vacancyId) {
        List<Resume> resumes = respondedApplicantService.getByVacancyId(vacancyId)
                .stream()
                .map(response -> resumeService.getById(response.getResumeId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(resumes);
    }
}