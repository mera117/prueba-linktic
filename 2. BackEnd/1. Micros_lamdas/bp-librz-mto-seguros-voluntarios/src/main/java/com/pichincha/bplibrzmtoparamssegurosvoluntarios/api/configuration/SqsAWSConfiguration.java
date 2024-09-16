package com.pichincha.bplibrzmtoparamssegurosvoluntarios.api.configuration;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsAWSConfiguration {

    @Value("${cloud.aws.sqs.queue-url-logs}")
    private String queueUrl;

    @Value("${cloud.aws.sqs.region}")
    private String region;

    public void sendMessage(String messageBody) {
        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard().withRegion(region).build();

        SendMessageRequest sendMsgRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);
        sqsClient.sendMessage(sendMsgRequest);
    }
}
