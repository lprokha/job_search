package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.RespondToVacancyDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.ConflictException;
import kg.attractor.job_search.exception.NotFoundException;
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

@RestController
@RequestMapping("/api/responses")
@RequiredArgsConstructor
public class ApiResponseController {

    private final RespondedApplicantService respondedApplicantService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    @PostMapping
    public ResponseEntity<RespondedApplicant> respondToVacancy(@RequestBody @Valid RespondToVacancyDto dto) {
        resumeService.getById(dto.getResumeId())
                .orElseThrow(() -> new NotFoundException("Resume not found with id = " + dto.getResumeId()));

        Vacancy vacancy = vacancyService.getById(dto.getVacancyId())
                .orElseThrow(() -> new NotFoundException("Vacancy not found with id = " + dto.getVacancyId()));

        if (!Boolean.TRUE.equals(vacancy.getIsActive())) {
            throw new BadRequestException("Cannot respond to inactive vacancy");
        }

        if (respondedApplicantService.existsByResumeIdAndVacancyId(dto.getResumeId(), dto.getVacancyId())) {
            throw new ConflictException("Response already exists for this resume and vacancy");
        }

        RespondedApplicant createdResponse = respondedApplicantService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResponse);
    }

    @GetMapping("/vacancy/{vacancyId}/applicants")
    public ResponseEntity<List<Resume>> getApplicantsByVacancy(@PathVariable Integer vacancyId) {
        vacancyService.getById(vacancyId)
                .orElseThrow(() -> new NotFoundException("Vacancy not found with id = " + vacancyId));

        return ResponseEntity.ok(resumeService.getApplicantsByVacancyId(vacancyId));
    }
}