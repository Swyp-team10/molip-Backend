package org.example.shallweeatbackend.controller;

import lombok.*;
import org.example.shallweeatbackend.dto.FileUploadRequest;
import org.example.shallweeatbackend.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final S3Service s3Service;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestBody FileUploadRequest request) {
        String base64File = request.getBase64File();
        String key = "uploads/" + System.currentTimeMillis() + ".webp";

        try {
            String fileUrl = s3Service.uploadBase64Image(bucketName, key, base64File);
            return ResponseEntity.ok().body(new UploadResponse(fileUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("파일 업로드를 실패했습니다.", e.getMessage()));
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("서버 내부 오류가 발생했습니다.", e.getMessage()));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadResponse {
        private String s3ImageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String error;
        private String message;
    }
}
