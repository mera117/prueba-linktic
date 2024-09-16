package com.pichincha.bplibrzmtoparamssegurosvoluntarios.api.configuration;


import static org.mockito.Mockito.*;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class DataSourceConfigurationTest {

    @Mock
    private AWSSecretsManager secretsManager;

    @Mock
    private GetSecretValueResult getSecretValueResult;

    @InjectMocks
    private DataSourceConfiguration dataSourceConfiguration;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDataSource_SdkClientException() throws IOException {
        // Set the properties using reflection
        try {
            setField(dataSourceConfiguration, "region", "us-east-1");
            setField(dataSourceConfiguration, "endpoint", "http://localhost:4584");
            setField(dataSourceConfiguration, "secretId", "dev/hubcon/postgres");
        } catch (Exception e) {
            fail("Failed to set fields via reflection");
        }

        // Simulate SdkClientException
        when(secretsManager.getSecretValue(any(GetSecretValueRequest.class))).thenThrow(new SdkClientException("AWS SDK Client Exception"));

        // Call the method to test and handle the exception
        assertThrows(SdkClientException.class, () -> dataSourceConfiguration.dataSource());
    }
}