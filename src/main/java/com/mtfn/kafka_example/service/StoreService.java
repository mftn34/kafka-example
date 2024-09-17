package com.mtfn.kafka_example.service;

import com.mtfn.kafka_example.constants.EventConstants;
import com.mtfn.kafka_example.domain.entity.Store;
import com.mtfn.kafka_example.domain.events.store.StoreEvent;
import com.mtfn.kafka_example.domain.events.store.StoreFailedEvent;
import com.mtfn.kafka_example.domain.events.store.StoreSuccessEvent;
import com.mtfn.kafka_example.domain.repository.StoreRepository;
import com.mtfn.kafka_example.enums.AggregateType;
import com.mtfn.kafka_example.service.kafka.producer.KafkaProducerService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService implements Serializable {

    private final KafkaProducerService<String, StoreEvent> kafkaProducerService;

    private final UnpublishedMessageService unpublishedMessageService;

    private final StoreRepository storeRepository;

    @Transactional
    public boolean isStoreExists(String storeCode) {
        var store = storeRepository.findByCode(storeCode).orElse(null);

        var event = createStoreEvent(store, storeCode);
        sendEvent(event);

        return store != null;
    }

    private StoreEvent createStoreEvent(Store store, String storeCode) {
        if (store != null) {
            return getStoreSuccessEvent(store);
        } else {
            return getStoreFailedEvent(storeCode);
        }
    }

    public StoreSuccessEvent getStoreSuccessEvent(Store store) {
        return StoreSuccessEvent.builder()
                .eventType(EventConstants.STORE_CREATED_UPDATED_SUCCESS)
                .changerUser("admin")
                .aggregateType(AggregateType.STORE.name())
                .code(store.getCode())
                .name(store.getName())
                .topic(EventConstants.STORE_CREATED_UPDATED_SUCCESS)
                .id("id")
                .build();
    }

    public StoreFailedEvent getStoreFailedEvent(String storeCode) {
        return StoreFailedEvent.builder()
                .failureMessages(List.of("Exception occurred at store create process"))
                .eventType(EventConstants.STORE_CREATED_UPDATED_FAILED)
                .code(storeCode)
                .topic(EventConstants.STORE_CREATED_UPDATED_FAILED)
                .build();
    }

    private void sendEvent(StoreEvent event) {
        kafkaProducerService.send(event.getTopic(), event.getCode(), event, unpublishedMessageService::saveFailEvent);
    }
}
