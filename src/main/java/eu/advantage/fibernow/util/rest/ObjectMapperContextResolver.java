package eu.advantage.fibernow.util.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;

    public ObjectMapperContextResolver() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("DateTimeModule");
        module.addSerializer(LocalDate.class, new LocalDateJsonSerializer());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeJsonSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateJsonDeserializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeJsonDeserializer());
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return objectMapper;
    }
}
