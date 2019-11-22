package com.gcgsauce.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceFoundException extends RuntimeException
{
    public ResourceFoundException(String message)
    {
        super(message);
    }

    public ResourceFoundException(String message,
                                  Throwable cause)
    {
        super(message,
              cause);
    }
}
