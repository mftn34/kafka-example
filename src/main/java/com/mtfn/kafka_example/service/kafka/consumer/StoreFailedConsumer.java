package com.mtfn.kafka_example.service.kafka.consumer;

import com.mtfn.kafka_example.domain.events.store.StoreFailedEvent;
import com.mtfn.kafka_example.domain.port.EventConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StoreFailedConsumer implements EventConsumer<StoreFailedEvent> {

    @Override
    @KafkaListener(topics =
            "${kafka.topics.consumer.store.failed}",
            autoStartup = "${kafka.consume.enable:true}",
            groupId = "${kafka.topics.consumer.group-id}"
    )
    public void consume(
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
            @Header(KafkaHeaders.OFFSET) String offset,
            @Payload StoreFailedEvent event) {
        try {
            log.info("Store failed event consume successfully");
        } catch (Exception e) {
            throw new KafkaException(
                    String.format(
                            "Exception occurred at store failed event for key: %s Ex: %s", event.getId(), e.getMessage()));
        }
    }
}