package eu.advantage.fibernow.util.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import eu.advantage.fibernow.exception.BaseError;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Value
public class ApiResponse<T> implements Serializable {
    String transactionId = UUID.randomUUID().toString().toUpperCase();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt = LocalDateTime.now();
    T data;
    Object baseErrors;
}
