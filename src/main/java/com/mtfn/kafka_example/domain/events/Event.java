package com.mtfn.kafka_example.domain.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mtfn.kafka_example.constants.EventConstants;
import com.mtfn.kafka_example.domain.events.store.StoreFailedEvent;
import com.mtfn.kafka_example.domain.events.store.StoreSuccessEvent;
import com.mtfn.kafka_example.enums.AggregateType;

import static com.mtfn.kafka_example.constants.DomainConstants.DEFAULT_JSON_OBJECT_FIELD;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = DEFAULT_JSON_OBJECT_FIELD, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StoreSuccessEvent.class, names = EventConstants.STORE_CREATED_UPDATED_SUCCESS),
        @JsonSubTypes.Type(value = StoreFailedEvent.class, names = EventConstants.STORE_CREATED_UPDATED_FAILED)
})
public interface Event {

    String getTopic();

    String getPayloadId();

    default String getAggregateType() {
        return AggregateType.STORE.name();
    }
}
