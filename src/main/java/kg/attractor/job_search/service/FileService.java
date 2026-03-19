package kg.attractor.job_search.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String saveUploadedFile(MultipartFile file, String subDir) throws IOException;
}