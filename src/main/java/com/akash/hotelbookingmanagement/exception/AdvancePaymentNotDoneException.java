package com.akash.hotelbookingmanagement.exception;

public class AdvancePaymentNotDoneException extends RuntimeException {

    public AdvancePaymentNotDoneException() {
        super();
    }

    public AdvancePaymentNotDoneException(String message) {
        super(message);
    }

    public AdvancePaymentNotDoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdvancePaymentNotDoneException(Throwable cause) {
        super(cause);
    }
}
