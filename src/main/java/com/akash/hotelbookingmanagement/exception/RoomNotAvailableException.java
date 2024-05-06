package com.akash.hotelbookingmanagement.exception;

public class RoomNotAvailableException extends RuntimeException {

    public RoomNotAvailableException() {
        super();
    }

    public RoomNotAvailableException(final String message) {
        super(message);
    }

}
