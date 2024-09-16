package com.pichincha.bplibrzmtorangotasas.api.configuration;

import static org.mockito.Mockito.*;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;


@ExtendWith(MockitoExtension.class)
public class DataSourceConfigurationTest {

    @Mock
    private AWSSecretsManager secretsManager;
    
    @Mock
    private SecretsManager SecretsManager;
    
    @InjectMocks
    private DataSourceConfiguration dataSourceConfiguration;
    
	@Value("${cloud.aws.secretmanager.secretname}")
	private String secretId;
    
    @BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);
        setField(dataSourceConfiguration, "region", "us-east-1");
        setField(dataSourceConfiguration, "endpoint", "secretsmanager.us-east-1.amazonaws.com\"");
        setField(dataSourceConfiguration, "secretId", secretId);
    }    
    
	@Test
	void testDataSourceCreation() {			
		when(SecretsManager.getSecret(secretId)).thenReturn(mockSecret());
		DriverManagerDataSource dataSource = (DriverManagerDataSource) dataSourceConfiguration.dataSource();
		assertNotNull(dataSource);
		assertEquals("jdbc:postgresql://localhost:5432/testdb?currentSchema=public", dataSource.getUrl());
		assertEquals("sa", dataSource.getUsername());
		assertEquals("sa", dataSource.getPassword());
	} 
    
    
    
	private Map<String, Object> mockSecret() {
		Map<String, Object> secret = new HashMap<>();
		secret.put("host", "localhost");
		secret.put("port", "5432");
		secret.put("db_name", "testdb");
		secret.put("schema_param", "public");
		secret.put("username", "sa");
		secret.put("password", "sa");
		return secret;
	}
}