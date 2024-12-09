package sales.application.sales.exceptions;

import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import sales.application.sales.dto.ErrorDto;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Logger;

@RestControllerAdvice
public class ExceptionAdvice {



    @Autowired
    Logger logger;

        @ExceptionHandler(value = {ObjectNotFoundException.class})
        @ResponseStatus(value = HttpStatus.NOT_FOUND)
        public ErrorDto resourceNotFoundException(ObjectNotFoundException ex, WebRequest request) {
            ErrorDto message = new ErrorDto(ex.getMessage(),404);
            logger.info(ex.getMessage());
            return message;
        }


    @Transactional
    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
        ErrorDto message = new ErrorDto(errorMessage,500);
        logger.info(ex.getMessage());
        return message;
    }

        @Transactional
        @ExceptionHandler(value = {NullPointerException.class})
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ErrorDto nullPointerException(NullPointerException ex, WebRequest request) {
            ErrorDto message = new ErrorDto("Something went wrong there is a null pointer exception.",500);
            logger.info(ex.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return message;
        }



    @Transactional
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        //String message = ex.getMessage().contains("constraint [null]") ? "Required parameters can't be null or a duplicate entry." : ex.getMessage();
        String errorMessage = getCauseMessage(ex);;
        errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
        ErrorDto err = new ErrorDto(errorMessage,400);
        logger.info(ex.getMessage());
        return err;
    }




        @ExceptionHandler(value = {HttpMessageNotReadableException.class})
        @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorDto httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
            ErrorDto message = new ErrorDto("May be request body is empty or required parameter are missing.",500);
            logger.info(ex.getMessage());
            return message;
        }


        @ExceptionHandler(value = {FileNotFoundException.class})
        @ResponseStatus(value = HttpStatus.NO_CONTENT)
        public ErrorDto fileNotFound(FileNotFoundException ex, WebRequest webRequest){
            ErrorDto message = new ErrorDto("File not found.",400);
            logger.info(ex.getMessage());
            return message;
        }



        @Transactional
        @ExceptionHandler(value = {ConstraintViolationException.class})
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ErrorDto duplicateEntryFound(ConstraintViolationException ex, WebRequest request) {
            String errorMessage = ex.getCause() != null ?  ex.getCause().getMessage() : ex.getMessage();
            errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
            ErrorDto err = new ErrorDto(errorMessage,400);
            logger.info(ex.getMessage());
            return err;
        }


        @Transactional
        @ExceptionHandler(value = {Exception.class})
        @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorDto resourceNotFoundException(Exception ex, WebRequest request) {
            ErrorDto message = null;
            try{
                String errorMessage = getCauseMessage(ex);
                errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
                message =new ErrorDto(errorMessage,500);
            }catch (Exception e) {
                ex.printStackTrace();
                String errorMessage = ex.getMessage();
                errorMessage = errorMessage.contains(";") ? errorMessage.substring(0, errorMessage.indexOf(";")) : errorMessage;
                message = new ErrorDto(errorMessage, 500);
            }
            logger.info(ex.getMessage());
            return message;
        }



    private String getCauseMessage(Throwable t){
        return t.getCause().getCause().getLocalizedMessage();
    }

    }



