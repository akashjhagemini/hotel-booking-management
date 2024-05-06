package com.akash.hotelbookingmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler that is responsible for handling exceptions across all the controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles the ResourceNotFoundException and returns appropriate response.
     *
     * @param exception      The ResourceNotFoundException that occurred.
     * @param request The current Web request.
     * @return a Response entity with error message and 404 status code.
     */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException exception, final WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage(), request.getDescription(false));
        LOGGER.debug(String.format("Resource not found exception %s", errorMessage));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the AdvancePaymentNotDoneException and returns an appropriate response.
     *
     * @param exception The AdvancePaymentNotDoneException that occurred.
     * @param request   The current Web request.
     * @return a Response entity with an error message and 400 status code.
     */
    @ExceptionHandler(AdvancePaymentNotDoneException.class)
    public ResponseEntity<Object> handleAdvancePaymentNotDoneException(final AdvancePaymentNotDoneException exception, final WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false));
        LOGGER.debug(String.format("Advance payment not done exception: %s", errorMessage));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the ChildrenNotAccompaniedByAdultException and returns an appropriate response.
     *
     * @param exception The ChildrenNotAccompaniedByAdultException that occurred.
     * @param request   The current Web request.
     * @return a Response entity with an error message and 400 status code.
     */
    @ExceptionHandler(ChildrenNotAccompaniedByAdultException.class)
    public ResponseEntity<Object> handleChildrenNotAccompaniedByAdultException(final ChildrenNotAccompaniedByAdultException exception, final WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false));
        LOGGER.debug(String.format("Children not accompanied by adult exception: %s", errorMessage));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the RoomNotAvailableException and returns an appropriate response.
     *
     * @param exception The RoomNotAvailableException that occurred.
     * @param request   The current Web request.
     * @return a Response entity with an error message and 400 status code.
     */
    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<Object> handleRoomNotAvailableException(final RoomNotAvailableException exception, final WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false));
        LOGGER.debug(String.format("Room not available exception: %s", errorMessage));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all exceptions and returns appropriate response.
     *
     * @param ex      The Exception that occurred.
     * @param request The current Web request.
     * @return a Response entity with error message and 500 status code.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(final Exception ex, final WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
        LOGGER.error(String.format("An Exception occurred %s ", errorMessage));
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Overrides the MethodArgumentNotValidException in ResponseEntityExceptionHandler
     * This method handles validation errors in request body or parameters.
     *
     * @param ex      The MethodArgumentNotValidException thrown when validation fails.
     * @param request The WebRequest object.
     * @return Response entity with custom error message with details.
     */


    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, "Validation error", request.getDescription(false));
        LOGGER.debug(String.format("MethodArgumentNotValidException %s", errorMessage));

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
