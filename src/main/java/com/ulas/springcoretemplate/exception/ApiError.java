package com.ulas.springcoretemplate.exception;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * this class for /error page.
 */
@Getter
@Setter
public class ApiError {
    private int status;
    private String message;
    private String path;

    private Map<String, String> errors;

    public ApiError(int status, String message, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
    }
}

