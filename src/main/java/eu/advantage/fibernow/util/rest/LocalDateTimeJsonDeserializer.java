package eu.advantage.fibernow.util.rest;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateStr = jsonParser.getText();
        LocalDateTime date = null;
        try {
            date = dateStr != null ? LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
        }
        catch (DateTimeParseException e) {
            // preserve null value
        }
        return date;
    }
}
