package eu.advantage.fibernow.exception;

public enum ExceptionStatus {

    BZ_ERROR_0001("Invalid Input [%s]", 1),
    BZ_ERROR_1001("Customer with [id=%s] does not exist.", 1),
    BZ_ERROR_1002("No customers found in the DB.", 0),
    BZ_ERROR_1003("Can not create Customer with [tin=%s]. There is already a Customer with the same Tax Identification Number", 1),
    BZ_ERROR_1004("Can not create Customer with [email=%s]. There is already a Customer with the same email", 1),
    BZ_ERROR_1005("Can not update Customer with [tin=%s]. There is already a Customer with the same Tax Identification Number", 1),
    BZ_ERROR_1006("Can not update Customer with [email=%s]. There is already a Customer with the same email", 1),
    BZ_ERROR_1007("Can not delete Customer with [id=%s]. They have open not completed / deleted tickets", 1),
    BZ_ERROR_1008("There are no Customers with [tin like: %s].", 1),
    BZ_ERROR_1009("There are no Customers with [email like: %s].", 1),
    BZ_ERROR_1010("There is already a Customer or an Admin with the [username=%s]", 1),

    BZ_ERROR_2001("Ticket with [id=%s] does not exist.", 1),
    BZ_ERROR_2002("Dates Not Valid : [Start Date = %s] and [End Date = %s]", 2),
    BZ_ERROR_2003("Ticket search with no parameters",0),
    BZ_ERROR_3001("Admin with [id=%s] does not exist.", 1),
    BZ_ERROR_3002("For Admin Insertion/Update : There is already a Customer or an Admin with the [username=%s]", 1),

    BZ_ERROR_4001("User with [username=%s] does not exist.", 1),

    BZ_ERROR_4002("Wrong password for User with [username=%s].", 1),
    BZ_ERROR_4003("Something went wrong with the login! Wrong [Username=%s] or password.",1),
    BZ_ERROR_4004("Found multiple users with the same [username=%s]! Contact admin",1);


    final String message;

    final Integer numberOfParams;

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
