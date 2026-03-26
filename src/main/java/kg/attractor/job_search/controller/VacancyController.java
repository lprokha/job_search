package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Vacancy> createVacancy(@RequestBody @Valid CreateVacancyDto dto) {
        if (dto.getExpTo() < dto.getExpFrom()) {
            throw new BadRequestException("Experience to cannot be less than experience from");
        }

        if (userService.findEmployer(dto.getAuthorId()).isEmpty()) {
            throw new ForbiddenException("User with id = " + dto.getAuthorId() + " is not an employer");
        }

        Vacancy createdVacancy = vacancyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVacancy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vacancy> updateVacancy(@PathVariable Integer id,
                                                 @RequestBody @Valid UpdateVacancyDto dto) {
        if (dto.getExpTo() < dto.getExpFrom()) {
            throw new BadRequestException("Experience to cannot be less than experience from");
        }

        if (userService.findEmployer(dto.getAuthorId()).isEmpty()) {
            throw new ForbiddenException("User with id = " + dto.getAuthorId() + " is not an employer");
        }

        Vacancy updatedVacancy = vacancyService.update(id, dto)
                .orElseThrow(() -> new NotFoundException("Vacancy not found with id = " + id));

        return ResponseEntity.ok(updatedVacancy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Integer id) {
        boolean deleted = vacancyService.delete(id);

        if (!deleted) {
            throw new NotFoundException("Vacancy not found with id = " + id);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/responded/applicant/{applicantId}")
    public ResponseEntity<List<Vacancy>> getRespondedVacanciesByApplicant(@PathVariable Integer applicantId) {
        return ResponseEntity.ok(vacancyService.getRespondedVacanciesByApplicantId(applicantId));
    }

    @GetMapping
    public ResponseEntity<List<Vacancy>> getAllVacancies() {
        return ResponseEntity.ok(vacancyService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Vacancy>> getAllActiveVacancies() {
        return ResponseEntity.ok(vacancyService.getAllActive());
    }

    @GetMapping("/active/category/{categoryId}")
    public ResponseEntity<List<Vacancy>> getActiveVacanciesByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(vacancyService.getActiveByCategory(categoryId));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Vacancy>> getVacanciesByAuthor(@PathVariable Integer authorId) {
        return ResponseEntity.ok(vacancyService.getByAuthorId(authorId));
    }
}