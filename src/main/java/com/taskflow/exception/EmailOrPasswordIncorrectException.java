package com.taskflow.exception;

public class EmailOrPasswordIncorrectException extends RuntimeException {

    public EmailOrPasswordIncorrectException(String message) {
        super(message);
    }
}
