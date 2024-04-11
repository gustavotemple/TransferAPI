package com.desafioitau.api.transferencia.config.exception;

import com.desafioitau.api.transferencia.config.exception.conta.ContaException;
import com.desafioitau.api.transferencia.config.exception.conta.ContaInactiveException;
import com.desafioitau.api.transferencia.config.exception.conta.ContaNotFoundException;
import com.desafioitau.api.transferencia.config.exception.conta.ContaSaldoInsufficientException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ClientNotFoundException.class,
            ContaNotFoundException.class})
    public ResponseEntity<Object> handleClientNotFoundException(
            Exception exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleServiceUnavailableException(
            ServiceUnavailableException exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({ContaException.class,
            ContaInactiveException.class,
            ContaInactiveException.class,
            ContaSaldoInsufficientException.class})
    public ResponseEntity<Object> handleContaException(
            Exception exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
