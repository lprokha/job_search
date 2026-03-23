package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.CreateUserDto;
import kg.attractor.job_search.dto.UpdateUserDto;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.FileService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> updateProfile(@PathVariable Integer id, @RequestBody UpdateUserDto dto) {
        return userService.updateProfile(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<List<User>> findUsersByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @GetMapping("/search/by-phone")
    public ResponseEntity<User> findUserByPhone(@RequestParam String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/by-email")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    @PostMapping
    public ResponseEntity<User> createAccount(@RequestBody CreateUserDto dto) {
        if (dto.getAccountType() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (userService.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User createdUser = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/employers/{id}")
    public ResponseEntity<User> findEmployer(@PathVariable Integer id) {
        return userService.findEmployer(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/applicants/{id}")
    public ResponseEntity<User> findApplicant(@PathVariable Integer id) {
        return userService.findApplicant(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> uploadAvatar(@PathVariable Integer userId,
                                             @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (userService.getById(userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            String fileName = fileService.saveUploadedFile(file, "avatars");

            return userService.updateAvatar(userId, fileName)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}