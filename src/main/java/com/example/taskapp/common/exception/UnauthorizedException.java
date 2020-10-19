package com.example.taskapp.common.exception;

/**
 * Boom Unauthorized Exception
 */
public class UnauthorizedException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     * The constructor.
     *
     * @param message the exception message
     */
    public UnauthorizedException(String message) {

        super(message);
    }

}
