package com.spring.databasebike.domain.awsS3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 이미지 한 장 업로드
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        // String originalFilename = multipartFile.getOriginalFilename();

        String uuid = "";
        String file_name = "";
        String file_path = "";

        if(!multipartFile.isEmpty()){
            uuid = UUID.randomUUID().toString();
            file_name = uuid + multipartFile.getOriginalFilename();
            file_path = "img/";
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, file_name, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, file_name).toString();
    }

    /**
     * 이미지 삭제
     * @param originalFilename
     */
    public void deleteImage(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }
    
}
