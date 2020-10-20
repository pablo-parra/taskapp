package com.example.taskapp.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Application Exception Handler
 */
@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {

        UUID uuid = UUID.randomUUID();
        ErrorResponse error = new ErrorResponse(ex.getMessage(), uuid);
        log.error(uuid.toString().concat(" | ").concat(error.getErrorMessage()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleBoomUnauthorizedException(UnauthorizedException ex) {

        UUID uuid = UUID.randomUUID();
        ErrorResponse error = new ErrorResponse(ex.getMessage(), uuid);
        log.info(uuid.toString().concat(" | ").concat(error.getErrorMessage()));
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        UUID uuid = UUID.randomUUID();
        ErrorResponse error = new ErrorResponse(errors.stream().findFirst().orElse(""), uuid);
        log.error(uuid.toString().concat(" | ").concat(error.getErrorMessage()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
}
