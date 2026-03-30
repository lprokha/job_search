package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.dto.UpdateUserDto;
import kg.attractor.job_search.exception.ConflictException;
import kg.attractor.job_search.exception.FileUploadException;
import kg.attractor.job_search.exception.ForbiddenException;
import kg.attractor.job_search.exception.NotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.FileService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final FileService fileService;

    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable Integer id,
                                              @RequestBody @Valid UpdateUserDto dto,
                                              Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));

        if (!currentUser.getId().equals(id)) {
            throw new ForbiddenException("You can update only your own profile");
        }

        if (!currentUser.getEmail().equalsIgnoreCase(dto.getEmail()) && userService.existsByEmail(dto.getEmail())) {
            throw new ConflictException("User with this email already exists");
        }

        User updatedUser = userService.updateProfile(id, dto)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + id));

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<List<User>> findUsersByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @GetMapping("/search/by-phone")
    public ResponseEntity<User> findUserByPhone(@RequestParam String phoneNumber) {
        User user = userService.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("User not found with phone number = " + phoneNumber));

        return ResponseEntity.ok(user);
    }

    @GetMapping("/search/by-email")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email = " + email));

        return ResponseEntity.ok(user);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    @PostMapping
    public ResponseEntity<User> createAccount(@RequestBody @Valid CreateUserDto dto) {
        if (userService.existsByEmail(dto.getEmail())) {
            throw new ConflictException("User with this email already exists");
        }

        User createdUser = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/employers/{id}")
    public ResponseEntity<User> findEmployer(@PathVariable Integer id) {
        User employer = userService.findEmployer(id)
                .orElseThrow(() -> new NotFoundException("Employer not found with id = " + id));

        return ResponseEntity.ok(employer);
    }

    @GetMapping("/applicants/{id}")
    public ResponseEntity<User> findApplicant(@PathVariable Integer id) {
        User applicant = userService.findApplicant(id)
                .orElseThrow(() -> new NotFoundException("Applicant not found with id = " + id));

        return ResponseEntity.ok(applicant);
    }

    @PostMapping(value = "/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> uploadAvatar(@PathVariable Integer userId,
                                             @RequestParam("file") MultipartFile file,
                                             Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));

        if (!currentUser.getId().equals(userId)) {
            throw new ForbiddenException("You can upload avatar only for your own account");
        }

        if (file.isEmpty()) {
            throw new kg.attractor.job_search.exception.BadRequestException("File cannot be empty");
        }

        try {
            String fileName = fileService.saveUploadedFile(file, "avatars");

            User updatedUser = userService.updateAvatar(userId, fileName)
                    .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

            return ResponseEntity.ok(updatedUser);

        } catch (IOException e) {
            throw new FileUploadException("Failed to upload avatar");
        }
    }
}