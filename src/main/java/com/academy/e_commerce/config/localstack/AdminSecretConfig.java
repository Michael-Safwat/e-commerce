//package com.academy.e_commerce.config.localstack;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.secretsmanager.AWSSecretsManager;
//import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
//import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
//import jakarta.annotation.PostConstruct;
//import lombok.Getter;
//import org.springframework.context.annotation.Configuration;
//
//@Getter
//@Configuration
//public class AdminSecretConfig {
//
//    private String adminName;
//    private String adminEmail;
//    private String adminPassword;
//
//    @PostConstruct
//    public void loadSecrets() throws Exception {
//        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
//                .withEndpointConfiguration(
//                        new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
//                .withCredentials(new AWSStaticCredentialsProvider(
//                        new BasicAWSCredentials("test", "test")))
//                .build();
//
//        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
//                .withSecretId("ecommerce-admin-secret");
//
//        String secretString = client.getSecretValue(getSecretValueRequest).getSecretString();
//
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode json = mapper.readTree(secretString);
//
//        this.adminName = json.get("admin.name").asText();
//        this.adminEmail = json.get("admin.email").asText();
//        this.adminPassword = json.get("admin.password").asText();
//    }
//}
