package com.taskflow.aop.handler;

import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.utils.CustomError;
import com.taskflow.utils.Response;
import com.taskflow.utils.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class AppExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Response<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Response<Object> response = new Response<>();
        List<CustomError> errorList = new ArrayList<>();
        response.setMessage("Validation error");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errorList.add(new CustomError(fieldName, errorMessage));
        });
        response.setErrors(errorList);
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFoundException.class)
    private Response<Object> handleValidationExceptions(ResourceNotFoundException ex) {
        Response<Object> response = new Response<>();
        List<CustomError> errorList = new ArrayList<>();
        response.setMessage("Resource not found");
        errorList.add(CustomError.builder()
                .field(ex.getField())
                .message(ex.getMessage())
                .build());
        response.setErrors(errorList);
        return response;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    private Response<Object> handleValidationExceptions(ValidationException ex) {
        Response<Object> response = new Response<>();
        List<CustomError> errorList = new ArrayList<>();
        response.setMessage("Validation error");
        errorList.add(CustomError.builder()
                .field(ex.getCustomError().getField())
                .message(ex.getCustomError().getMessage())
                .build());
        response.setErrors(errorList);
        return response;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Response<Object>> handleValidationExceptions(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.internalServerError().body(Response.builder()
                .message("Internal server error")
                        .result(ex.getMessage())
                .build());
    }
}
