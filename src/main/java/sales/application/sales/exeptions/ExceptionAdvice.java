package sales.application.sales.exeptions;

import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import sales.application.sales.dto.ErrorDto;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ExceptionAdvice {


        @ExceptionHandler(value = {ObjectNotFoundException.class})
        @ResponseStatus(value = HttpStatus.NOT_FOUND)
        public ErrorDto resourceNotFoundException(ObjectNotFoundException ex, WebRequest request) {
            ErrorDto message = new ErrorDto(ex.getMessage(),404);
            ex.printStackTrace();
            return message;
        }


        @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ErrorDto resourceNotFoundException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
            ErrorDto message = new ErrorDto(ex.getMessage(),500);
            ex.printStackTrace();
            return message;
        }

        @Transactional
        @ExceptionHandler(value = {NullPointerException.class})
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ErrorDto resourceNotFoundException(NullPointerException ex, WebRequest request) {
            ErrorDto message = new ErrorDto("Something went wrong there is a null pointer exception.",500);
            ex.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return message;
        }




        @ExceptionHandler(value = {DataIntegrityViolationException.class})
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ErrorDto dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
            String message = ex.getMessage().contains("constraint [null]") ? "Required parameters can't be null." : ex.getMessage();
            ErrorDto err = new ErrorDto(message,400);
            ex.printStackTrace();
            return err;
        }



        @ExceptionHandler(value = {HttpMessageNotReadableException.class})
        @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorDto httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
            ErrorDto message = new ErrorDto("May be request body is empty or required parameter are missing.",500);
            ex.printStackTrace();
            return message;
        }

        @Transactional
        @ExceptionHandler(value = {Exception.class})
        @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorDto resourceNotFoundException(Exception ex, WebRequest request) {
            ErrorDto message = new ErrorDto(ex.getMessage(),500);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ex.printStackTrace();
            return message;
        }

    }



