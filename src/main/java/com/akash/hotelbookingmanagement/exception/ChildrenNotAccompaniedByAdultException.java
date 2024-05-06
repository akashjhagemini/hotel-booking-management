package com.akash.hotelbookingmanagement.exception;

public class ChildrenNotAccompaniedByAdultException extends RuntimeException {

    public ChildrenNotAccompaniedByAdultException() {
        super();
    }

    public ChildrenNotAccompaniedByAdultException(final String message) {
        super(message);
    }

}
