package eu.advantage.fibernow.exception;

public class BusinessException extends RuntimeException{
    private ExceptionStatus status;

    public BusinessException(ExceptionStatus status, Object...params) {
        super(String.format(status.getMessage(), params));
        this.status = status;
    }

    public BusinessException(ExceptionStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public ExceptionStatus getStatus() {
        return status;
    }
}
