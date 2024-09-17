package com.mtfn.kafka_example.util;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

@Component
public class ObjectMapperUtil {

    private static final Logger log = LoggerFactory.getLogger(ObjectMapperUtil.class);

    private final ObjectMapper objectMapper;

    public ObjectMapperUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Exception at ObjectMapperUtil.toJson: {}", object, e);
            return null;
        }
    }

    public <T> T messageToObject(Class<T> destinationType, String message) {
        try {
            return objectMapper.readerFor(destinationType).readValue(message);
        } catch (Exception e) {
            log.error("Exception at ObjectMapperUtil.messageToObject: {}", message, e);
            return null;
        }
    }

    public <T> T extractTreeValue(T value, Type type) {
        try {
            var javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.treeToValue((TreeNode) value, javaType);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
