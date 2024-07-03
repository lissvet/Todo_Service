package com.example.todo_service.rest_controller;


import com.example.todo_service.entity.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Server Error");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.getErrors().add(e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Invalid data");
        errorResponse.setStatus(400);
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.getErrors().add(fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
