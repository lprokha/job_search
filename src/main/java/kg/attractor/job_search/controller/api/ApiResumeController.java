package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.AccountType;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ApiResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Resume> createResume(@RequestBody @Valid CreateResumeDto dto,
                                               Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can create resumes");
        }

        Resume createdResume = resumeService.create(
                CreateResumeDto.builder()
                        .name(dto.getName())
                        .categoryId(dto.getCategoryId())
                        .salary(dto.getSalary())
                        .isActive(dto.getIsActive())
                        .build(),
                currentUser.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdResume);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(@PathVariable Integer id,
                                               @RequestBody @Valid UpdateResumeDto dto,
                                               Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));

        if (currentUser.getAccountType() != AccountType.APPLICANT) {
            throw new ForbiddenException("Only applicants can update resumes");
        }

        Resume existingResume = resumeService.getById(id)
                .orElseThrow(() -> new NotFoundException("Resume not found with id = " + id));

        if (!existingResume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can update only your own resume");
        }

        Resume updatedResume = resumeService.update(id, dto)
                .orElseThrow(() -> new NotFoundException("Resume not found with id = " + id));

        return ResponseEntity.ok(updatedResume);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Integer id,
                                             Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));

        Resume existingResume = resumeService.getById(id)
                .orElseThrow(() -> new NotFoundException("Resume not found with id = " + id));

        if (!existingResume.getApplicantId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can delete only your own resume");
        }

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