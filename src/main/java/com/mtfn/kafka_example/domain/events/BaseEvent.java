package com.mtfn.kafka_example.domain.events;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = -6539852083858194399L;

    @JsonAlias({"key"})
    private String id;

    private String eventType;

    private String aggregateType;

    @JsonIgnore
    private String payloadId;
}
