package com.pichincha.bplibrzmtorangotasas.api.configuration;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestPropertySource(properties = { "cloud.aws.secretmanager.endpoint=secretsmanager.us-east-2.amazonaws.com",
		"cloud.aws.secretmanager.region=us-east-2" })
@ContextConfiguration(classes = AwsTestConfig.class)
public class SecretsManagerTest {

	@Mock
	private AWSSecretsManager secretsManagerClient;

	@InjectMocks
	private SecretsManager secretsManager;

	@Value("${cloud.aws.secretmanager.region}")
	private String region;
	@Value("${cloud.aws.secretmanager.endpoint}")
	private String endpoint;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetSecret() {

		SecretsManager secretsManagerSpy = new SecretsManager() {
			@Override
			protected AWSSecretsManager createSecretsManagerClient() {
				return secretsManagerClient;
			}
		};

		String secretId = "my-secret";
		String secretString = "{\"key\": \"value\"}";
		GetSecretValueResult secretValueResult = new GetSecretValueResult().withSecretString(secretString);

		when(secretsManagerClient.getSecretValue(new GetSecretValueRequest().withSecretId(secretId)))
				.thenReturn(secretValueResult);

		Map<String, Object> result = secretsManagerSpy.getSecret(secretId);

		Map<String, Object> expected = new HashMap<>();
		expected.put("key", "value");
		assertEquals(expected, result);
	}
	
	@Test
	void createSecretsManagerClientTest() 
	{
		SecretsManager secretsManagerSpy = new SecretsManager() {
			@Override
			protected AWSSecretsManager createSecretsManagerClient() {
				return secretsManagerClient;
			}
		};
		
		assertTrue(secretsManagerSpy.createSecretsManagerClient() != null);
	}


	@Test
	public void testCreateSecretsManagerClient() throws Exception {
	    ReflectionTestUtils.setField(secretsManager, "region", "us-west-2");
	    ReflectionTestUtils.setField(secretsManager, "endpoint", "https://secretsmanager.us-west-2.amazonaws.com");
	    Method method = SecretsManager.class.getDeclaredMethod("createSecretsManagerClient");
	    method.setAccessible(true);
	    AWSSecretsManager client = (AWSSecretsManager) method.invoke(secretsManager);

	    assertNotNull(client, "The client should not be null");
	}

}
