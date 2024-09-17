package com.mtfn.kafka_example.domain.events.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class StoreSuccessEvent extends StoreEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 8386984984723518960L;

    private String changerUser;
}
