package com.mtfn.kafka_example.service.kafka.producer;

import com.mtfn.kafka_example.enums.KafkaMessageStatus;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerService<K extends Serializable, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Retryable(retryFor = KafkaException.class, backoff = @Backoff(delay = 300L))
    public CompletableFuture<SendResult<K, V>> send(String topicName, K key, V message,
                                                    BiConsumer<Map<String, V>, KafkaMessageStatus> kafkaMessageStatus) {
        var kafkaResultFuture = new CompletableFuture<SendResult<K, V>>();
        try {
            kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.whenComplete((kvSendResult, throwable) -> {
                if (throwable == null) {
                    kafkaMessageStatus.accept(Map.of(StringUtils.EMPTY, message), KafkaMessageStatus.COMPLETED);

                } else {
                    kafkaMessageStatus.accept(Map.of(throwable.getMessage(), message), KafkaMessageStatus.IN_PROGRESS);
                }
            });
        } catch (KafkaException e) {
            kafkaMessageStatus.accept(Map.of(e.getMessage(), message), KafkaMessageStatus.FAILED);
        }
        return kafkaResultFuture;
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            kafkaTemplate.destroy();
        }
    }
}