package eu.advantage.fibernow.exception;

import eu.advantage.fibernow.util.rest.ResponseUtils;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<BaseError> errors = e.getConstraintViolations()
                .stream()
                .map(item -> {
                    BaseError error = new BaseError();
                    error.setCode(ExceptionStatus.BZ_ERROR_0001);
                    error.setMessage(String.format(ExceptionStatus.BZ_ERROR_0001.getMessage(), item.getMessage()));
                    return error;
                }).collect(Collectors.toList());
        return ResponseUtils.errorResponse(errors, Response.Status.BAD_REQUEST);
    }
}
