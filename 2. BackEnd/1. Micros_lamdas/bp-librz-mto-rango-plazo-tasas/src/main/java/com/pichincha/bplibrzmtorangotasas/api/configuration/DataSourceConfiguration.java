package com.pichincha.bplibrzmtorangotasas.api.configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class DataSourceConfiguration {

    @Value("${cloud.aws.secretmanager.region}")
    public String region;

    @Value("${cloud.aws.secretmanager.endpoint}")
    public String endpoint;

    @Value("${cloud.aws.secretmanager.secretname}")
    public String secretId;
    
	private final SecretsManager secretsManager;
	
	public DataSourceConfiguration(SecretsManager secretsManager) {
		this.secretsManager = secretsManager;
	}

    @Bean
    public DataSource dataSource() {
        //Map<String, Object> secret = getSecretValue(secretId);
        Map<String, Object> secret = secretsManager.getSecret(secretId);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");

        dataSource.setUrl("jdbc:postgresql://" + secret.get("host") + ":" + secret.get("port") + "/" + secret.get("db_name") + "?currentSchema=" + secret.get("schema_param"));
        dataSource.setUsername(secret.get("username").toString());
        dataSource.setPassword(secret.get("password").toString());

        return dataSource;
    }
}
