package eu.advantage.fibernow.util.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.advantage.fibernow.util.rest.LocalDateJsonDeserializer;
import eu.advantage.fibernow.util.rest.LocalDateJsonSerializer;
import eu.advantage.fibernow.util.rest.LocalDateTimeJsonDeserializer;
import eu.advantage.fibernow.util.rest.LocalDateTimeJsonSerializer;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.core.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ResponseUtils {
    public static <T> Response errorResponse(T obj, Response.Status status) {
        return Response.status(status).entity(toJsonString(obj)).build();
    }

    public static <T> Response errorResponse(Response.Status status) {
        return Response.status(status).build();
    }

    public static <T> Response genericErrorResponse(T obj) {
        return errorResponse(obj, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public static <T> Response successResponse() {
        return Response.ok().build();
    }

    public static <T> Response successResponse(T obj) {
        return Response.ok(toJsonString(obj)).build();
    }

    public static <T> String toJsonString(T object){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("DateTimeModule");
            module.addSerializer(LocalDate.class, new LocalDateJsonSerializer());
            module.addSerializer(LocalDateTime.class, new LocalDateTimeJsonSerializer());
            module.addDeserializer(LocalDate.class, new LocalDateJsonDeserializer());
            module.addDeserializer(LocalDateTime.class, new LocalDateTimeJsonDeserializer());
            objectMapper.registerModule(module);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting response to JSON.");
        }
    }
}
