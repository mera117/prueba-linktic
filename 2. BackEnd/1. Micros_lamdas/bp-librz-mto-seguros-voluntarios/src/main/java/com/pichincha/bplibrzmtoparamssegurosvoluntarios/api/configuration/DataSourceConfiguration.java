package com.pichincha.bplibrzmtoparamssegurosvoluntarios.api.configuration;


import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class DataSourceConfiguration {

    @Value("${cloud.aws.secretmanager.region}")
    private String region;

    @Value("${cloud.aws.secretmanager.endpoint}")
    private String endpoint;

    @Value("${cloud.aws.secretmanager.secretname}")
    private String secretId;

    @Bean
    public DataSource dataSource() throws IOException {

        Map<String, Object> secret = getSecretValue(secretId);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://" + secret.get("host") + ":" + secret.get("port") + "/" + secret.get("db_name") + "?currentSchema="+secret.get("schema_param"));
        dataSource.setUsername(secret.get("username").toString());
        dataSource.setPassword(secret.get("password").toString());

        return dataSource;
    }

    public Map<String, Object> getSecretValue(String secretId) throws JsonProcessingException {
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);

        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        AWSSecretsManager client = clientBuilder.build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretId);
        GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        String secretString = getSecretValueResult.getSecretString();
        try {
            return new ObjectMapper().readValue(secretString, Map.class);
        } catch (Exception e) {
            // Handle exception
            return new HashMap<>();
        }
    }
}
