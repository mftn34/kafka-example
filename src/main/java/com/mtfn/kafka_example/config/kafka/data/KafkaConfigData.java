package com.mtfn.kafka_example.config.kafka.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigData {

    private Integer numOfPartitions;

    private Short replicationFactor;
}
