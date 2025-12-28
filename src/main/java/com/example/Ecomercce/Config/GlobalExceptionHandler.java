package com.example.Ecomercce.Config;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.Ecomercce.DTOs.ErrorDTOs.ErrorResponseDTO;
import com.example.Ecomercce.Exceptions.AlreadyExistsException;
import com.example.Ecomercce.Exceptions.DatabaseErrorException;
import com.example.Ecomercce.Exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import com.example.Ecomercce.Exceptions.InvalidRequestException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.slf4j.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDTO>handleInvalidRequestException(InvalidRequestException ex, HttpServletRequest request){
        ErrorResponseDTO error= new ErrorResponseDTO(ex.getMessage(),
    request.getRequestURL().toString());
        
        logger.warn("(Status Code 400) Petición inválida error_message={} url={} method={}" ,ex.getMessage(), request.getRequestURL(),request.getMethod());
        
        return new ResponseEntity<>(error,
            HttpStatus.BAD_REQUEST);}

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO>handleNotFoundException(NotFoundException ex, HttpServletRequest request){
        ErrorResponseDTO error= new ErrorResponseDTO(ex.getMessage(),request.getRequestURL().toString());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        

    }

    
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO>handleAlreadyExistsException(AlreadyExistsException ex, HttpServletRequest request){
        ErrorResponseDTO error= new ErrorResponseDTO(ex.getMessage(),request.getRequestURL().toString());
        
        logger.warn("(Status Code 409) Registro duplicado error_message={} url={} method={}" ,ex.getMessage(), request.getRequestURL(),request.getMethod());
        
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
        ErrorResponseDTO error= new ErrorResponseDTO(ex.getMessage(),request.getRequestURL().toString());
         logger.warn("(Status Code 400) Error en la validación error_message={} url={} method={}" ,ex.getMessage(), request.getRequestURL(),request.getMethod());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DatabaseErrorException.class)
    public ResponseEntity<ErrorResponseDTO>handleDatabaseErrorException(DatabaseErrorException ex, HttpServletRequest request){
        ErrorResponseDTO error= new ErrorResponseDTO(ex.getMessage(),request.getRequestURL().toString());
        logger.error("(Status Code 500) Error inesperado en el servidor error_message={} url={} method={}" ,ex.getMessage(), request.getRequestURL(), request.getMethod());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

        
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO>handleInternalServerError(HttpServletRequest request, Exception ex){
        ErrorResponseDTO error= new ErrorResponseDTO("Error interno en el servidor",request.getRequestURL().toString());
        
        logger.error("(Status Code 500) Error inesperado en el servidor error_message={} url={} method={}" ,ex.getMessage(), request.getRequestURL(),request.getMethod());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
