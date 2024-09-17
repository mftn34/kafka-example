package com.mtfn.kafka_example.domain.events.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mtfn.kafka_example.domain.events.BaseEvent;
import com.mtfn.kafka_example.domain.events.Event;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class StoreEvent extends BaseEvent implements Serializable, Event {

    @Serial
    private static final long serialVersionUID = 2007759275813619653L;

    private String code;

    private String name;

    private String aggregateType;

    @JsonIgnore
    private String topic;
}
