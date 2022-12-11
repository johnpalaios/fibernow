package eu.advantage.fibernow.exception;

import java.io.Serializable;

public class BaseError implements Serializable {
    private ExceptionStatus code;

    private String message;

    public ExceptionStatus getCode() {
        return code;
    }

    public void setCode(ExceptionStatus code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
