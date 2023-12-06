package com.mabaya.ads.controller;

import com.mabaya.ads.dto.ExceptionResponse;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
   * Handles validation failures for request payload.
   *
   * @param ex The MethodArgumentNotValidException encountered.
   * @return A {@link ResponseEntity} with the validation error details and a BAD_REQUEST status.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();

    String errorMessage =
        result.getFieldErrors().stream()
            .map(fe -> String.format("[%s: %s]", fe.getField(), fe.getDefaultMessage()))
            .collect(Collectors.joining(", "));

    LOGGER.error("Caught a {}: {}", ex.getClass().getSimpleName(), errorMessage);
    return getExceptionResponseResponseEntity(HttpStatus.BAD_REQUEST, errorMessage);
  }

  /**
   * Handles {@link IllegalArgumentException} to provide a clear response when an illegal argument
   * is passed.
   *
   * @param ex The IllegalArgumentException encountered.
   * @return A {@link ResponseEntity} with the exception's message and a BAD_REQUEST status.
   */
  @ExceptionHandler({
    MethodArgumentTypeMismatchException.class,
    ConversionFailedException.class,
    IllegalArgumentException.class
  })
  // TODO javadoc
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    final String errorMessage = ex.getMessage();
    LOGGER.error("Caught a {}: {}", ex.getClass().getSimpleName(), errorMessage);
    return getExceptionResponseResponseEntity(HttpStatus.BAD_REQUEST, errorMessage);
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
  public ResponseEntity<ExceptionResponse> handleNoSuchElementException(NoSuchElementException ex) {
    final String errorMessage = ex.getMessage();
    LOGGER.error("No such element: {}", errorMessage);
    return getExceptionResponseResponseEntity(HttpStatus.NOT_FOUND, errorMessage);
  }

  private ResponseEntity<ExceptionResponse> getExceptionResponseResponseEntity(
      HttpStatus httpStatus, String errorMessage) {
    return new ResponseEntity<>(
        new ExceptionResponse(httpStatus.getReasonPhrase(), errorMessage, Instant.now()),
        httpStatus);
  }
}
