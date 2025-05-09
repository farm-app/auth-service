package ru.rtln.authservice.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import ru.rtln.common.model.ErrorModel;

@Slf4j
@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorModel> authServiceException(HttpClientErrorException exception) {
        log.error("HttpClientErrorException occurred", exception);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorModel(HttpStatus.UNAUTHORIZED.value(), "HttpClientErrorException", exception.getResponseBodyAsString()));
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorModel> authServiceException(MissingRequestCookieException exception) {
        log.error("MissingRequestCookieException occurred", exception);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorModel(HttpStatus.UNAUTHORIZED.value(), "MissingRequestCookieException", exception.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorModel> authServiceException(BadCredentialsException exception) {
        log.error("BadCredentialsException occurred", exception);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorModel(HttpStatus.UNAUTHORIZED.value(), "BadCredentialsException", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> authServiceException(Exception exception) {
        log.error("Exception occurred", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception", exception.getMessage()));
    }
}
