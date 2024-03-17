package com.acr.orderservice.kafka;

import com.acr.basedomains.dto.OrderEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class OrderProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

    private NewTopic topic;

    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(NewTopic topic, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderEvent event) {

        LOGGER.info(String.format("Order event => %s", event.toString()));

        // create Message
        Message<OrderEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        CompletableFuture<SendResult<String, OrderEvent>> send = kafkaTemplate.send(message);

        System.out.println("Message sent ! Waiting for response...");

        send.whenComplete((result, err) -> {

            System.out.println("Send result when complete : ");

            if (err != null) {

                // LOGGING

                System.out.println("An error occurred {} " + err.getMessage());

            } else if (result != null) {

                System.out.println(result.getProducerRecord().toString());
                System.out.println(result.getRecordMetadata().toString());

            }

        });


//        SendResult<String, OrderEvent> stringOrderEventSendResult = send.join();
//        System.out.println(stringOrderEventSendResult.getProducerRecord().topic());

        System.out.println("Kafka send operation done !");


    }
}
