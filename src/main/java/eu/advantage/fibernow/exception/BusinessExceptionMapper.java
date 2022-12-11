package eu.advantage.fibernow.exception;

import eu.advantage.fibernow.util.rest.ResponseUtils;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException e) {
        BaseError error = new BaseError();
        error.setMessage(e.getMessage());
        error.setCode(e.getStatus());
        return ResponseUtils.errorResponse(error, Response.Status.BAD_REQUEST);
    }
}
