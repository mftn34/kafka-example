package com.mtfn.kafka_example.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@UtilityClass
public class StringUtil {

    public static String cropAtMaxSize(String text, int maxSize) {
        try {
            if (!StringUtils.hasLength(text)) {
                return text;
            }
            return text.length() > maxSize ? text.substring(0, maxSize) : text;
        } catch (Exception e) {
            log.info("Exception occurred at cropAtMaxSize; text: {}; maxSize: {}; ", text, maxSize, e);
            return text;
        }
    }
}
