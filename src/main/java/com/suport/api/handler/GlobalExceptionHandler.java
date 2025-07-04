package com.suport.api.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.suport.api.exceptions.BadRequestException;
import com.suport.api.exceptions.TokenGenerationException;
import com.suport.api.exceptions.TokenValidationException;
import com.suport.api.exceptions.defaultExceptionDetails.ExceptionDetails;
import com.suport.api.exceptions.details.BadRequestExceptionDetails;
import com.suport.api.exceptions.details.TokenGenerationExceptionDetails;
import com.suport.api.exceptions.details.TokenValidationExceptionDetails;
import com.suport.api.exceptions.details.ValidationExceptionDetails;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(
            BadRequestExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                // Tradução: Exceção de requisição inválida. Verifique a documentação.
                .title("Bad Request Exception - Check the documentation")
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .details(exception.getClass().getName())
                .build(),
            HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String fieldsMessage = fieldErrors.stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(","));
        String fields = fieldErrors.stream()
            .map(FieldError::getField)
            .collect(Collectors.joining(","));

        return new ResponseEntity<>(
            ValidationExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .title("Field validation error - Check the documentation")
                .status(HttpStatus.BAD_REQUEST.value())
                .details(exception.getClass().getName())
                .fields(fields)
                .fieldsMessage(fieldsMessage)
                .message(exception.getMessage())
                .build(),
            HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception exception,
        @Nullable Object body,
        HttpHeaders headers,
        HttpStatusCode statusCode,
        WebRequest request
    ) {
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
            .timestamp(LocalDateTime.now())
            .title(exception.getCause() != null ? exception.getCause().getMessage() : "Internal Exception")
            .status(statusCode.value())
            .message(exception.getMessage())
            .details(exception.getClass().getName())
            .build();

        return new ResponseEntity<>(exceptionDetails, headers, statusCode);
    }

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<TokenGenerationExceptionDetails> handleTokenGeneration(TokenGenerationException ex) {
        return new ResponseEntity<>(
            TokenGenerationExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .title("Error generating JWT token")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .details(ex.getClass().getName())
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<TokenValidationExceptionDetails> handleTokenValidation(TokenValidationException ex) {
        return new ResponseEntity<>(
            TokenValidationExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .title("Invalid JWT token")
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .details(ex.getClass().getName())
                .build(),
            HttpStatus.UNAUTHORIZED
        );
    }
}
