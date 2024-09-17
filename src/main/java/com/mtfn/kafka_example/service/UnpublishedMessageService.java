package com.mtfn.kafka_example.service;

import com.mtfn.kafka_example.domain.events.store.StoreEvent;
import com.mtfn.kafka_example.domain.repository.UnpublishedMessageRepository;
import com.mtfn.kafka_example.domain.entity.UnpublishedMessage;
import com.mtfn.kafka_example.enums.AggregateType;
import com.mtfn.kafka_example.enums.KafkaMessageStatus;
import com.mtfn.kafka_example.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnpublishedMessageService {

    private final UnpublishedMessageRepository unpublishedMessageRepository;

    private final ObjectMapperUtil objectMapperUtil;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T extends StoreEvent> void saveFailEvent(Map<String, T> eventMap, KafkaMessageStatus kafkaMessageStatus) {
        if (KafkaMessageStatus.FAILED.equals(kafkaMessageStatus)) {
            eventMap.forEach((message, event) -> {
                var unpublishedMessage = UnpublishedMessage.builder()
                        .active(Boolean.TRUE)
                        .retryCount(0)
                        .correlationId(event.getCode())
                        .topic(event.getTopic())
                        .aggregateType(
                                AggregateType.valueOf(event.getAggregateType()))
                        .payload(objectMapperUtil.toJson(event))
                        .kafkaMessageStatus(KafkaMessageStatus.FAILED)
                        .failureMessage(message)
                        .build();
                unpublishedMessageRepository.save(unpublishedMessage);
            });
        }
    }
}

