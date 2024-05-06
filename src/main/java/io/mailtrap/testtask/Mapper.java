package io.mailtrap.testtask;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class Mapper {
    private static final ObjectMapper mapper = JsonMapper.builder()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .serializationInclusion(JsonInclude.Include.NON_EMPTY)
            .build();

    public static ObjectMapper get() {
        return mapper;
    }
}
