package com.pichincha.bplibrzmtorangotasas.api.configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class SecretsManager {

	@Value("${cloud.aws.secretmanager.region}")
	private String region;
	@Value("${cloud.aws.secretmanager.endpoint}")
	private String endpoint;

	@SuppressWarnings("unchecked")
	public Map<String, Object> getSecret(String secretId) {
		AWSSecretsManager client = createSecretsManagerClient();
		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretId);
		GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);
		String secretString = getSecretValueResult.getSecretString();
		try {
			return new ObjectMapper().readValue(secretString, Map.class);
		} catch (Exception e) {
			return new HashMap<String, Object>();
		}
	}

	protected AWSSecretsManager createSecretsManagerClient() {
		AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
		AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
		clientBuilder.setEndpointConfiguration(config);
		return clientBuilder.build();
	}
	
}
