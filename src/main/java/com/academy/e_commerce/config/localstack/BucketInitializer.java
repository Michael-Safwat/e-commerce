package com.academy.e_commerce.config.localstack;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BucketInitializer implements CommandLineRunner {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public BucketInitializer(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void run(String... args) {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
            log.info("Created bucket: {}", bucketName);
        } else {
            log.info("Bucket already exists: {}", bucketName);
        }
    }
}
