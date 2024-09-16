package com.pichincha.bplibrzmtorangotasas.api.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;

@Configuration
public class AwsTestConfig {
	@Bean
	@Primary
	public AWSSecretsManager mockAWSSecretsManager() {
		return Mockito.mock(AWSSecretsManager.class);
	}
}
