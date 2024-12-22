package com.onion.backend.handler;

import com.onion.backend.exception.NotAllowedException;
import com.onion.backend.exception.RateLimitException;
import com.onion.backend.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFountException(ResourceNotFoundException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotAllowedException(NotAllowedException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleRateLimitException(RateLimitException ex){
        return ex.getMessage();
    }

}
