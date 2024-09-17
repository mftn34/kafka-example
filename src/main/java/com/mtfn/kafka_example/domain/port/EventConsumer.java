package com.mtfn.kafka_example.domain.port;

@FunctionalInterface
public interface EventConsumer<T> {

    void consume(String topic, String partition, String offset, T message);
}
