package com.gabrielscheibler.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError
{

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    public ApiError()
    {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status)
    {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, String message)
    {
        this();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus()
    {
        return status;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public String getMessage()
    {
        return message;
    }
}