package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.CreateUserDto;
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

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<User> createAccount(@RequestBody CreateUserDto dto) {
        if (dto.getAccountType() == null) {
            return ResponseEntity.badRequest().build();
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
            String fileName = fileService.saveUploadedFile(file, "/avatars");
            User updatedUser = userService.updateAvatar(userId, fileName);
            return ResponseEntity.ok(updatedUser);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}