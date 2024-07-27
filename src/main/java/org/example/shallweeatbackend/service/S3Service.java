package org.example.shallweeatbackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    public String uploadBase64Image(String bucketName, String key, String base64Image) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(decodedBytes.length);
        metadata.setContentType("image/webp"); // webp MIME 타입 설정

        amazonS3.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));

        return amazonS3.getUrl(bucketName, key).toString();
    }
}

