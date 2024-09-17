package com.mtfn.kafka_example.config.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.mtfn.kafka_example.config.kafka.data.KafkaConsumerConfigData;
import com.mtfn.kafka_example.converter.KafkaMessageConverter;
import com.mtfn.kafka_example.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static com.mtfn.kafka_example.constants.DomainConstants.*;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    private final KafkaPropertiesConfiguration kafkaPropertiesConfiguration;

    private final ObjectMapperUtil objectMapper;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPropertiesConfiguration.getKafkaServerAddresses());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigData.getAutoOffsetReset());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerConfigData.getSessionTimeoutMs());
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getHeartbeatIntervalMs());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getMaxPollIntervalMs());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerConfigData.getMaxPollRecords());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
                kafkaConsumerConfigData.getMaxPartitionFetchBytesDefault()
                        * kafkaConsumerConfigData.getMaxPartitionFetchBytesBoostFactor());
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, true);
        return props;
    }

    @Bean
    public ConsumerFactory<String, JsonNode> consumerFactory() {
        JsonDeserializer<JsonNode> deserializer = new JsonDeserializer<>(JsonNode.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, JsonNode> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, JsonNode> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(kafkaConsumerConfigData.getBatchListener());
        factory.setConcurrency(kafkaConsumerConfigData.getConcurrencyLevel());
        factory.setAutoStartup(kafkaConsumerConfigData.getAutoStartup());
        factory.setCommonErrorHandler(errorHandler(kafkaTemplate));
        factory.getContainerProperties().setPollTimeout(kafkaConsumerConfigData.getPollTimeoutMs());
        factory.setRecordMessageConverter(new KafkaMessageConverter(objectMapper));
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<K, V> template) {
        var recoverer = new DeadLetterPublishingRecoverer(template,
                (cr, e) -> new TopicPartition(cr.topic()
                        + kafkaConsumerConfigData.getDeadletterPrefix(),
                        1));
        var exponentialBackOff = new ExponentialBackOffWithMaxRetries(2);
        exponentialBackOff.setInitialInterval(ERROR_HANDLER_INITIAL_INTERVAL);
        exponentialBackOff.setMultiplier(ERROR_HANDLER_MULTIPLIER);
        exponentialBackOff.setMaxInterval(ERROR_HANDLER_MAX_INTERVAL);
        var errorHandler = new DefaultErrorHandler(recoverer, exponentialBackOff);
        // if you want not retry this exception then add not retry
        // errorHandler.addNotRetryableExceptions(A.class, B.class,
        // jakarta.validation.ValidationException.class);
        errorHandler.setLogLevel(KafkaException.Level.ERROR);
        return errorHandler;
    }
}