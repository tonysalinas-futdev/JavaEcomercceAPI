package com.example.Ecomercce.Config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.Ecomercce.DTOs.ErrorDTOs.ErrorResponseDTO;
import com.example.Ecomercce.Exceptions.AlreadyExistsException;
import com.example.Ecomercce.Exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import com.example.Ecomercce.Exceptions.InvalidRequestException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDTO>handleInvalidRequestException(InvalidRequestException ex, HttpServletRequest request){
        ErrorResponseDTO error= new ErrorResponseDTO(ex.getMessage(),
    request.getRequestURL().toString());
        
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
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
        ErrorResponseDTO error= new ErrorResponseDTO(ex.getMessage(),request.getRequestURL().toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO>handleInternalServerError(HttpServletRequest request){
        ErrorResponseDTO error= new ErrorResponseDTO("Error interno en el servidor",request.getRequestURL().toString());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
