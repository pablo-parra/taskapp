package com.example.taskapp.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Error Response object
 */
@Getter
@Setter
public class ErrorResponse {
    private String errorMessage;

    private String timestamp;

    private UUID uuid;

    /**
     * The constructor.
     *
     * @param errorMessage is the message inside the response
     */
    public ErrorResponse(String errorMessage) {

        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        this.uuid = UUID.randomUUID();
    }

    /**
     * @param errorMessage the message
     * @param uuid         predefined uuid
     */
    public ErrorResponse(String errorMessage, UUID uuid) {

        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        this.uuid = uuid;
    }
}
