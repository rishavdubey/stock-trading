package com.example.stocktrading.exception;

public class CustomException extends RuntimeException{

    private String message;

    private String errorCode;

    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(String message,String errorCode) {
        this.message = message;
        this.errorCode=errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}