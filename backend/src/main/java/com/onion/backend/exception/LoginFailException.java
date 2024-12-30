package com.onion.backend.exception;

public class LoginFailException extends RuntimeException {

    public LoginFailException(String message){
        super(message);
    }

}
