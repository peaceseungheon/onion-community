package com.onion.backend.exception;

public class NotAllowedException extends RuntimeException {

    public NotAllowedException(String message){
        super(message);
    }

}
