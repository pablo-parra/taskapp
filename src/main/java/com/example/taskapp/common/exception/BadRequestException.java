package com.example.taskapp.common.exception;

/**
 * Bad Request Exception
 */
public class BadRequestException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constructor.
     *
     * @param message the exception message
     */
    public BadRequestException(String message) {

        super(message);
    }
}
