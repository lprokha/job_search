package kg.attractor.job_search.controller;

import kg.attractor.job_search.exception.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class FileController {

    @GetMapping("/uploads/avatars/{fileName}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String fileName) throws IOException {
        Path path = Path.of("uploads", "avatars", fileName);

        if (!Files.exists(path)) {
            throw new NotFoundException("Файл изображения не найден");
        }

        String contentType = Files.probeContentType(path);

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(Files.readAllBytes(path));
    }
}