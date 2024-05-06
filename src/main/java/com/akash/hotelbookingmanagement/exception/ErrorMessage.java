package com.akash.hotelbookingmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Represents an error message that is to be returned in case of exceptions.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    /**
     * The HTTP Status of the error.
     */
    private HttpStatus status;
    /**
     * The message associated with the error.
     */
    private String message;
    /**
     * The api path where the error has occurred.
     */
    private String path;
}
