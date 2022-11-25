package eu.advantage.fibernow.exception;

public enum ExceptionStatus {

    BZ_ERROR_0001("Invalid Input [%s]", 1);

    String message;

    Integer numberOfParams;

    ExceptionStatus(String message, Integer numberOfParams) {
        this.message = message;
        this.numberOfParams = numberOfParams;
    }

    public String getMessage() {
        return message;
    }

    public Integer getNumberOfParams() {
        return numberOfParams;
    }
}
