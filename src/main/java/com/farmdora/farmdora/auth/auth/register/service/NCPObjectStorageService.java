package com.farmdora.farmdora.auth.auth.register.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class NCPObjectStorageService implements NCPStorageService {

    @Value("${ncp.end-point}")
    private String endPoint;
    @Value("${ncp.region-name}")
    private String regionName;
    @Value("${ncp.access-key}")
    private String accessKey;
    @Value("${ncp.secret-key}")
    private String secretKey;
    @Value("${ncp.bucket-name}")
    private String bucketName;

    private AmazonS3 s3;

    @PostConstruct
    public void init() {
        System.out.println("init ncp 호출됨");

        System.getProperties().setProperty("aws.java.v1.disableDeprecationAnnouncement","true");
        this.s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }


    @Override
    public void upload(String filePath, InputStream fileIn, long fileSize) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileSize); // ✅ 필수
        objectMetadata.setContentType("application/octet-stream"); // 또는 실제 타입

        filePath ="seller/" + filePath;

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, fileIn, objectMetadata);

        try{
            s3.putObject(putObjectRequest);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
