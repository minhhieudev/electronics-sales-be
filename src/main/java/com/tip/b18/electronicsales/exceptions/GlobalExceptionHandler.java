package com.tip.b18.electronicsales.exceptions;

import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseDTO<?> errorResponse(String message) {
        return new ResponseDTO<>("error", message, null, null);
    }

    @ExceptionHandler(value = CredentialsException.class)
    ResponseEntity<ResponseDTO<?>> handleCredentialsAndUnauthorizedException(CredentialsException e){
        ResponseDTO<?> responseDTO = errorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    ResponseEntity<ResponseDTO<?>> handlingAlreadyExistsException(Exception e){
        ResponseDTO<?> responseDTO = errorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ResponseDTO<?>> handlingAccessDeniedException(AccessDeniedException e){
        ResponseDTO<?> responseDTO = errorResponse(MessageConstant.ERROR_ACCESS_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
    }

    @ExceptionHandler({InvalidPasswordException.class, MethodArgumentNotValidException.class, NotFoundException.class, MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ResponseDTO<?>> handleBadRequest(Exception e) {
        String message;
        if (e instanceof MethodArgumentNotValidException) {
            message = Objects.requireNonNull(((MethodArgumentNotValidException) e).getFieldError()).getDefaultMessage();
        } else if(e instanceof MissingServletRequestParameterException){
            message = MessageConstant.ERROR_VALUE_REQUIRED;
        } else if(e instanceof MethodArgumentTypeMismatchException){
            message = MessageConstant.INVALID_UUID;
        } else{
            message = e.getMessage();
        }
        ResponseDTO<?> responseDTO = errorResponse(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

}
