package com.mtfn.kafka_example.config.kafka.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka.producer-config")
public class KafkaProducerConfigData {

    private String acks;

    private Integer batchSize;

    private Integer batchSizeBoostFactor;

    private Integer lingerMs;

    private Integer requestTimeoutMs;

    private Integer retryCount;
}