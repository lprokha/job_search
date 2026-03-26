package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Resume> createResume(@RequestBody @Valid CreateResumeDto dto) {
        if (userService.findApplicant(dto.getApplicantId()).isEmpty()) {
            throw new ForbiddenException("User with id = " + dto.getApplicantId() + " is not an applicant");
        }

        Resume createdResume = resumeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResume);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(@PathVariable Integer id,
                                               @RequestBody @Valid UpdateResumeDto dto) {
        if (userService.findApplicant(dto.getApplicantId()).isEmpty()) {
            throw new ForbiddenException("User with id = " + dto.getApplicantId() + " is not an applicant");
        }

        Resume updatedResume = resumeService.update(id, dto)
                .orElseThrow(() -> new NotFoundException("Resume not found with id = " + id));

        return ResponseEntity.ok(updatedResume);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Integer id) {
        boolean deleted = resumeService.delete(id);

        if (!deleted) {
            throw new NotFoundException("Resume not found with id = " + id);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        return ResponseEntity.ok(resumeService.getAll());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Resume>> getResumesByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(resumeService.getByCategory(categoryId));
    }

    @GetMapping("/applicant/{applicantId}")
    public ResponseEntity<List<Resume>> getResumesByApplicant(@PathVariable Integer applicantId) {
        return ResponseEntity.ok(resumeService.getByApplicantId(applicantId));
    }
}