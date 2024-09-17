package com.mtfn.kafka_example.util;

import com.mtfn.kafka_example.annotation.Crop;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@UtilityClass
public class FieldUtil {

    private static final Logger log = LoggerFactory.getLogger(FieldUtil.class);

    public static void cropValueWithAnnotation(Object entity) {
        FieldUtils.getFieldsListWithAnnotation(entity.getClass(), Crop.class).forEach(field -> {
            field.setAccessible(true);
            try {
                int maxSize = field.getAnnotation(Crop.class).value();
                Object fieldValue = field.get(entity);
                if (Objects.isNull(fieldValue)) {
                    return;
                }
                field.set(entity, StringUtil.cropAtMaxSize(fieldValue.toString(), maxSize));
            } catch (IllegalAccessException e) {
                log.info("Exception occurred while cropping field: {}; ", field.getName(), e);
            }
        });
    }
}