package br.com.builders.cliente.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ValidationErrorResponse handlerOnConstraintValidationException(ConstraintViolationException e) {
        var error = new ValidationErrorResponse();

        e.getConstraintViolations().forEach(violation -> error.getViolations()
                .add(new Violation(violation.getPropertyPath().toString(), violation.getMessage())));
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse handlerOnMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var error = new ValidationErrorResponse();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> error.getViolations()
                .add(new Violation(fieldError.getField(), fieldError.getDefaultMessage())));

        return error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    GenericErrorResponse handlerOnIllegalArgumentException(IllegalArgumentException e) {
        return handlerError(e.getLocalizedMessage());
    }

    @ExceptionHandler(ClienteRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    GenericErrorResponse handlerOnClienteRuntimeException(ClienteRuntimeException e) {
        return GenericErrorResponse.builder()
                .message(e.getLocalizedMessage())
                .build();
    }

    private GenericErrorResponse handlerError(String message) {
        return GenericErrorResponse.builder()
                .message(message)
                .build();
    }
}
