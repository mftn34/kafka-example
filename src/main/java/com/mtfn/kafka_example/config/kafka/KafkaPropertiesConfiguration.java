package com.mtfn.kafka_example.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class KafkaPropertiesConfiguration {

    @Value("${kafka.server.addresses}")
    private String kafkaServerAddresses;

    @Value("${kafka.topics.producer.store.success}")
    private String storeSuccessTopic;

    @Value("${kafka.topics.producer.store.failed}")
    private String storeFailedTopic;

    @Value("${kafka.topics.partition-value}")
    private Integer topicPartitionValue;

    @Value("${kafka.topics.replica-value}")
    private Integer topicReplicaValue;
}
