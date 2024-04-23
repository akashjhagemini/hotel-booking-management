package com.akash.hotelbookingmanagement.exception;

public class RoomNotAvailableException extends RuntimeException {

    public RoomNotAvailableException() {
        super();
    }

    public RoomNotAvailableException(String message) {
        super(message);
    }

    public RoomNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoomNotAvailableException(Throwable cause) {
        super(cause);
    }
}
