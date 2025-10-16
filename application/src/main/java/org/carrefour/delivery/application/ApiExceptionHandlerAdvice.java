package org.carrefour.delivery.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.carrefour.delivery.domain.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandlerAdvice {

    private static final Logger LOG = LogManager.getLogger(ApiExceptionHandlerAdvice.class.getName());

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> business(BusinessException exception, WebRequest request) {
        LOG.error(exception.getMessage(), exception);
        return ResponseEntity.unprocessableEntity().body(exception.getMessage());
    }
}
