package com.pichincha.bplibrzmtoparamssegurosvoluntarios.api.configuration;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

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
        // Simular una excepción al enviar el mensaje
        doThrow(RuntimeException.class).when(amazonSQS).sendMessage(any(SendMessageRequest.class));

        // Llamar al método que debería lanzar la excepción
        try {
            sqsAWSConfiguration.sendMessage("Test message");
            // Si no se lanza la excepción, el test falla
            assert false;
        } catch (RuntimeException e) {
            // Verificar que se lanzó la excepción esperada
            assert true;
        }
    }
}