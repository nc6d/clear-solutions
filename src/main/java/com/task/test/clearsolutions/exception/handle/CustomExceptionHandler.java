package com.task.test.clearsolutions.exception.handle;

import com.task.test.clearsolutions.exception.Status409UserAlreadyRegisteredException;
import com.task.test.clearsolutions.exception.Status410UserNotExistsException;
import com.task.test.clearsolutions.exception.Status431UserIsUnderAgeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({Status409UserAlreadyRegisteredException.class, Status431UserIsUnderAgeException.class})
    public ResponseEntity<ApiError> handle(RuntimeException ex) {
        log.error(ex.getClass() + " : " + ex.getMessage());
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getMessage());

        return ResponseEntity
                .status(error.getStatus())
                .body(error);

    }

    @ExceptionHandler({Status410UserNotExistsException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        log.error(ex.getClass() + " : " + ex.getMessage());
        ApiError error = new ApiError(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());

        return ResponseEntity
                .status(error.getStatus())
                .body(error);

    }
}
