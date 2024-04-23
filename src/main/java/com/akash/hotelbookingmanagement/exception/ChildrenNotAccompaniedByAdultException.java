package com.akash.hotelbookingmanagement.exception;

public class ChildrenNotAccompaniedByAdultException extends RuntimeException {

    public ChildrenNotAccompaniedByAdultException() {
        super();
    }

    public ChildrenNotAccompaniedByAdultException(String message) {
        super(message);
    }

    public ChildrenNotAccompaniedByAdultException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChildrenNotAccompaniedByAdultException(Throwable cause) {
        super(cause);
    }
}
