package dku.cloudcomputing.memberserver.controller;

import dku.cloudcomputing.memberserver.controller.dto.response.FieldBindingErrorDto;
import dku.cloudcomputing.memberserver.controller.dto.response.BindingErrorResponseDto;
import dku.cloudcomputing.memberserver.controller.dto.response.StatusResponseDto;
import dku.cloudcomputing.memberserver.exception.ClientOccurException;
import dku.cloudcomputing.memberserver.exception.FieldBindException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvise {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FieldBindException.class)
    public BindingErrorResponseDto fieldBindExceptionHandler(FieldBindException bindException) {
        BindingErrorResponseDto bindingErrorResponseDto = new BindingErrorResponseDto("fail");
        bindingErrorResponseDto.getErrors().addAll(bindException.getFieldErrors().stream()
                .map(e -> new FieldBindingErrorDto(e.getField(), e.getDefaultMessage()))
                .toList());
        return bindingErrorResponseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClientOccurException.class)
    public StatusResponseDto fieldNotMatchPasswordExceptionHandler(ClientOccurException clientOccurException) {
        return new StatusResponseDto("fail");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
    public StatusResponseDto fieldDBIntegrityExceptionHandler(
            JdbcSQLIntegrityConstraintViolationException integrityViolationException) {
        return new StatusResponseDto("fail");
    }
}
