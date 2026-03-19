package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.CreateVacancyDto;
import kg.attractor.job_search.dto.UpdateVacancyDto;
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
    public ResponseEntity<Vacancy> createVacancy(@RequestBody CreateVacancyDto dto) {
        if (dto.getAuthorId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (userService.findEmployer(dto.getAuthorId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Vacancy createdVacancy = vacancyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVacancy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vacancy> updateVacancy(@PathVariable Integer id,
                                                 @RequestBody UpdateVacancyDto dto) {
        if (dto.getAuthorId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (userService.findEmployer(dto.getAuthorId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return vacancyService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Integer id) {
        boolean deleted = vacancyService.delete(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
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