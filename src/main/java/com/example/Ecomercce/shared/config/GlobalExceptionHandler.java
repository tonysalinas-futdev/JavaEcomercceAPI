package com.example.Ecomercce.shared.config;

import com.example.Ecomercce.auth.exceptions.CredentialsException;
import com.example.Ecomercce.auth.exceptions.InvalidTokenException;
import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.shared.DTOs.errorDTOs.ErrorResponseDTO;
import com.example.Ecomercce.shared.exceptions.AlreadyExistsException;
import com.example.Ecomercce.shared.exceptions.InvalidRequestException;
import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.shared.exceptions.PersistenceErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
  private final LoggerService logger;

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidRequestException(
      InvalidRequestException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());

    logger.createWarnLog(ex.getMessage(), ex.getClass().toString(), "400");

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(
      NotFoundException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());
    logger.createWarnLog(ex.getMessage(), ex.getClass().toString(), "404");
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDTO> handleAlreadyExistsException(
      AlreadyExistsException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());

    logger.createWarnLog(ex.getMessage(), ex.getClass().toString(), "409");

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());
    logger.createWarnLog(ex.getMessage(), ex.getClass().toString(), "400");

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PersistenceErrorException.class)
  public ResponseEntity<ErrorResponseDTO> handleDatabaseErrorException(
      PersistenceErrorException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());
    logger.createErrorLog(ex.getMessage(), ex.getClass().toString(), ex, "500");
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleInternalServerError(
      HttpServletRequest request, Exception ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO("Error interno en el servidor", request.getRequestURL().toString());

    logger.createErrorLog(ex.getMessage(), ex.getClass().toString(), ex, "500");
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidTokenException(
      HttpServletRequest request, Exception ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());
    logger.createErrorLog(ex.getMessage(), ex.getClass().toString(), ex, "403");
    return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(CredentialsException.class)
  public ResponseEntity<ErrorResponseDTO> handleCredentialsException(
      HttpServletRequest request, Exception ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());
    logger.createErrorLog(ex.getMessage(), ex.getClass().toString(), ex, "403");
    return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponseDTO> handleDataAccesException(
      HttpServletRequest request, DataAccessException ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO("Error interno del servidor", request.getRequestURL().toString());
    logger.createErrorLog(ex.getMessage(), ex.getClass().toString(), ex, "500");
    return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
