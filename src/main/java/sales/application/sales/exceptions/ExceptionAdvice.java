package sales.application.sales.exceptions;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.transaction.Transactional;
import sales.application.sales.dto.ErrorDto;

@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDto notFoundException(NotFoundException ex, WebRequest request) {
        logger.error("NotFoundException: {}", ex.getMessage(), ex);
        ErrorDto message = new ErrorDto(ex.getMessage(), 404);
        return message;
    }

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDto resourceNotFoundException(ObjectNotFoundException ex, WebRequest request) {
        logger.error("ObjectNotFoundException: {}", ex.getMessage(), ex);
        ErrorDto message = new ErrorDto(ex.getMessage(), 404);
        return message;
    }

    @Transactional
    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        logger.error("SQLIntegrityConstraintViolationException: {}", ex.getMessage(), ex);
        String errorMessage = ex.getMessage();
        errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
        ErrorDto message = new ErrorDto(errorMessage, 500);
        return message;
    }

    @Transactional
    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto nullPointerException(NullPointerException ex, WebRequest request) {
        logger.error("NullPointerException: {}", ex.getMessage(), ex);
        ErrorDto message = new ErrorDto("Something went wrong there is a null pointer exception.", 500);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return message;
    }

    @Transactional
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("DataIntegrityViolationException: {}", ex.getMessage(), ex);
        String errorMessage = getCauseMessage(ex);
        errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
        ErrorDto err = new ErrorDto(errorMessage, 400);
        return err;
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        logger.error("HttpMessageNotReadableException: {}", ex.getMessage(), ex);
        ErrorDto message = new ErrorDto("May be request body is empty or required parameter are missing.", 500);
        return message;
    }

    @ExceptionHandler(value = {FileNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ErrorDto fileNotFound(FileNotFoundException ex, WebRequest webRequest) {
        logger.error("FileNotFoundException: {}", ex.getMessage());
        ErrorDto message = new ErrorDto("File not found.", 400);
        return message;
    }

    @Transactional
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto duplicateEntryFound(ConstraintViolationException ex, WebRequest request) {
        logger.error("ConstraintViolationException: {}", ex.getMessage(), ex);
        String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
        ErrorDto err = new ErrorDto(errorMessage, 400);
        return err;
    }

    @Transactional
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto resourceNotFoundException(Exception ex, WebRequest request) {
        logger.error("Exception: {}", ex.getMessage(), ex);
        ErrorDto message = null;
        try {
            String errorMessage = getCauseMessage(ex);
            errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
            message = new ErrorDto(errorMessage, 500);
        } catch (Exception e) {
            ex.printStackTrace();
            String errorMessage = ex.getMessage();
            errorMessage = errorMessage != null && errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
            message = new ErrorDto(errorMessage, 500);
        }
        return message;
    }

    private String getCauseMessage(Throwable t) {
        return t.getCause().getCause().getLocalizedMessage();
    }
}



