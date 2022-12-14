package com.ulas.springcoretemplate.exception;


import com.ulas.springcoretemplate.constant.ErrorConstants;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 6158443734275883814L;
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CustomException(ErrorConstants errorConstants) {
        super(errorConstants.getCodes());
        this.status = errorConstants.getHttpStatus();
    }

    public CustomException(String errorCode) {
        super(errorCode);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
