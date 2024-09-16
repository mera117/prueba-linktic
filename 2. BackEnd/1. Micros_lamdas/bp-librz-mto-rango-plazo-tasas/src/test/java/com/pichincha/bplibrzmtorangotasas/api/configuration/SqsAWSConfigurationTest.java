package com.pichincha.bplibrzmtorangotasas.api.configuration;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;

class SqsAWSConfigurationTest {

    @Mock
    private AmazonSQS amazonSQS;

    private SqsAWSConfiguration sqsAWSConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        sqsAWSConfiguration = new SqsAWSConfiguration();
    }

    @Test
    void testSendMessage_Exception() {
        doThrow(RuntimeException.class).when(amazonSQS).sendMessage(any(SendMessageRequest.class));

        try {
            sqsAWSConfiguration.sendMessage("Test message");
            assert false;
        } catch (RuntimeException e) {
            assert true;
        }
    }
}