package eu.advantage.fibernow.exception;

public enum ExceptionStatus {

    BZ_ERROR_0001("Invalid Input [%s]", 1),
    BZ_ERROR_1001("Customer [id=%s] does not exist.", 1),
    BZ_ERROR_1002("2 Customers with this email or tin.", 0),

    BZ_ERROR_2001("Ticket [id=%s] does not exist.", 1),
    BZ_ERROR_2002("Dates Not Valid : [Start Date = %s] and [End Date = %s]", 2),
    BZ_ERROR_2003("Ticket search with no parameters",0);
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
