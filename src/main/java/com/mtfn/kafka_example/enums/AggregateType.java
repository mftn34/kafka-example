package com.mtfn.kafka_example.enums;

import java.util.EnumSet;

public enum AggregateType {

    STORE;

    public static AggregateType getAggregateType(String type) {
        return EnumSet.allOf(AggregateType.class)
                .stream().filter(aggregateType -> aggregateType.name().equalsIgnoreCase(type))
                .findFirst().orElse(null);
    }
}
