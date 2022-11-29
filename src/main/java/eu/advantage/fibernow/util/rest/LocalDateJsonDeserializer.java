package eu.advantage.fibernow.util.rest;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateJsonDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateStr = jsonParser.getText();
        LocalDate date = null;
        try {
            date = dateStr != null ? LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE) : null;
        }
        catch (DateTimeParseException e) {
            // preserve null value
        }
        return date;
    }
}
