package kg.attractor.job_search.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private static final String UPLOAD_DIR = "uploads";

    public String saveUploadedFile(MultipartFile file, String subDir) throws IOException {
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "_" + file.getOriginalFilename();

        Path pathDir = Paths.get(UPLOAD_DIR + subDir);
        Files.createDirectories(pathDir);

        Path filePath = pathDir.resolve(resultFileName);

        try (OutputStream os = Files.newOutputStream(filePath)) {
            os.write(file.getBytes());
        }

        return resultFileName;
    }
}