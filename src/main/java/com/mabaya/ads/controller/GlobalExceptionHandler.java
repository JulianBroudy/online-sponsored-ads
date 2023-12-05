package com.mabaya.ads.controller;

import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler for the application. This class uses {@link ControllerAdvice} to handle
 * exceptions across the whole application, providing a central point to manage error responses.
 *
 * <p>It offers handling for common exceptions such as {@link MethodArgumentTypeMismatchException},
 * {@link ConversionFailedException}, {@link IllegalArgumentException}, and {@link
 * NoSuchElementException}, ensuring a consistent response format for various error scenarios.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 * @see ResponseEntity
 */
@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles cases where a method argument's type does not match, resulting in a {@link
   * MethodArgumentTypeMismatchException}.
   *
   * @param ex The exception encountered.
   * @return A {@link ResponseEntity} with an error message and a BAD_REQUEST status.
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    String error = "Invalid value for parameter '" + ex.getName() + "': " + ex.getValue();
    LOGGER.error(error);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles cases where a value conversion fails, leading to a {@link ConversionFailedException}.
   *
   * @param ex The exception encountered.
   * @return A {@link ResponseEntity} with an error message and a BAD_REQUEST status.
   */
  @ExceptionHandler(ConversionFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleConversionFailed(ConversionFailedException ex) {
    String error = "Failed to convert value: " + ex.getValue() + " for " + ex.getTargetType();
    LOGGER.error(error);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link IllegalArgumentException} to provide a clear response when an illegal argument
   * is passed.
   *
   * @param ex The IllegalArgumentException encountered.
   * @return A {@link ResponseEntity} with the exception's message and a BAD_REQUEST status.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    LOGGER.error("Illegal argument exception: {}", ex.getMessage());
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link NoSuchElementException}, providing a NOT_FOUND response when an element is not
   * found.
   *
   * @param ex The NoSuchElementException encountered.
   * @return A {@link ResponseEntity} with the exception's message and a NOT_FOUND status.
   */
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
    LOGGER.error("No such element: {}", ex.getMessage());
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}
