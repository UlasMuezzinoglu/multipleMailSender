package com.ulas.springcoretemplate.exception;


import com.ulas.springcoretemplate.util.MethodUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Exceptions with instance of CustomException fall here and maps by own values
     *
     * @param exception as CustomException for setting prepareErrorJSON with status of exception
     * @param request   WebRequest
     * @return Response entity as prepared exception response
     */
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Object> applicationException(CustomException exception, WebRequest request) {
        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(exception.getStatus(), exception), exception.getStatus());
    }
    // error handle for @Valid

    /**
     * Exceptions with instance of MethodArgumentNotValid fall here and maps by own values
     *
     * @param ex      as MethodArgumentNotValidException for setting list of error with values of exception
     * @param headers HttpHeaders for return to frontend on headers
     * @param status  HttpStatus for setting status code of body
     * @param request WebRequest
     * @return Response entity as prepared exception response
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        body.put("code", errors.get(0));
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    /**
     * Exceptions with instance of BindException fall here and maps by own values
     *
     * @param ex      as BindException for setting list of error with values of exception
     * @param headers as HttpHeaders for return to frontend on headers
     * @param status  as HttpStatus for setting status code of body
     * @param request WebRequest
     * @return Response entity as prepared exception response
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        if (errors.size() == 0)
            errors = getAllErrorsIfFieldErrorsIsEmpty(ex);
        body.put("code", errors.get(0));
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    private List<String> getAllErrorsIfFieldErrorsIsEmpty(BindException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
        return errors;
    }

    /**
     * Exceptions with instance of MissingServletRequestParameter fall here and maps by own values
     *
     * @param ex      as MissingServletRequestParameterException for setting message of body object
     * @param headers as HttpHeaders for return to frontend on headers
     * @param status  as HttpStatus for setting status code of body
     * @param request WebRequest
     * @return Response entity as prepared exception response
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());


        body.put("errors", ex.getMessage());

        return new ResponseEntity<>(body, headers, status);
    }

    /**
     * Exceptions with instance of ConstraintViolationException fall here and maps by own values
     *
     * @param ex      as ConstraintViolationException for settings list of error object
     * @param request WebRequest
     * @return Response entity as prepared exception response
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exceptions with instance of MethodArgumentTypeMismatchException fall here and maps by own values
     *
     * @param ex      of MethodArgumentTypeMismatchException for Used to show true and false usage types
     * @param request WebRequest
     * @return Response entity as prepared exception response
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exceptions with instance of HttpRequestMethodNotSupported fall here and maps by own values
     *
     * @param ex      as HttpRequestMethodNotSupportedException for Used show true and false usage types
     * @param headers as HttpHeaders
     * @param status  as HttpStatus
     * @param request WebRequest
     * @return Response entity as prepared exception response
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);

    }

    /**
     * Exceptions with instance of HttpMediaTypeNotSupported fall here and maps by own values
     *
     * @param ex      as HttpMediaTypeNotSupportedException for Used show true and false usage types
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return Response entity as prepared exception response
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex), HttpStatus.UNSUPPORTED_MEDIA_TYPE);

    }

    /**
     * Exceptions with instance of Exception fall here and maps by own values
     *
     * @param ex      as Exception sent to method prepareErrorJSON for processing
     * @param request WebRequest
     * @return Response entity as prepared exception response
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
    }
}

