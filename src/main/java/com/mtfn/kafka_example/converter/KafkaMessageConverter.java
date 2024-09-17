package com.mtfn.kafka_example.converter;

import com.mtfn.kafka_example.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.converter.MessagingMessageConverter;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class KafkaMessageConverter extends MessagingMessageConverter {

    private final ObjectMapperUtil objectMapper;

    @Override
    protected Object extractAndConvertValue(ConsumerRecord<?, ?> consumerRecord, Type type) {
        Object value = consumerRecord.value();
        return objectMapper.extractTreeValue(value, type);
    }
}