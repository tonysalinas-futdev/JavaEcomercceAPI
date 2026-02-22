package com.example.ecommerce.shared.config;

import com.example.ecommerce.auth.exceptions.CredentialsException;
import com.example.ecommerce.auth.exceptions.InvalidTokenException;
import com.example.ecommerce.shared.dtos.error.ErrorResponseDTO;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import com.example.ecommerce.shared.exceptions.NotFoundException;
import com.example.ecommerce.shared.exceptions.PersistenceErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidRequestException(
      InvalidRequestException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(
      NotFoundException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDTO> handleAlreadyExistsException(
      AlreadyExistsException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PersistenceErrorException.class)
  public ResponseEntity<ErrorResponseDTO> handleDatabaseErrorException(
      PersistenceErrorException ex, HttpServletRequest request) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleInternalServerError(
      HttpServletRequest request, Exception ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO("Error interno en el servidor", request.getRequestURL().toString());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidTokenException(
      HttpServletRequest request, Exception ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());

    return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(CredentialsException.class)
  public ResponseEntity<ErrorResponseDTO> handleCredentialsException(
      HttpServletRequest request, Exception ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());

    return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(
      HttpServletRequest request, org.springframework.security.access.AccessDeniedException ex) {

    ErrorResponseDTO error =
        new ErrorResponseDTO("Acces denegated", request.getRequestURL().toString());

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(
      HttpServletRequest request, Exception ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO(ex.getMessage(), request.getRequestURL().toString());
    return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponseDTO> handleDataAccesException(
      HttpServletRequest request, DataAccessException ex) {
    ErrorResponseDTO error =
        new ErrorResponseDTO("Error interno del servidor", request.getRequestURL().toString());
    return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
