package com.mtfn.kafka_example.domain.events.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class StoreFailedEvent extends StoreEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = -2740122307634975811L;

    private List<String> failureMessages;
}
