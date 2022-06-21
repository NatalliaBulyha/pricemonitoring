package by.senla.training.bulyha.pricemonitoring.handler;

import by.senla.training.bulyha.pricemonitoring.ErrorDto;
import by.senla.training.bulyha.pricemonitoring.exception.CsvException;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.exception.AuthException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;


@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, InternalException.class, IllegalArgumentException.class,
            CsvException.class, UnexpectedTypeException.class})
    protected ResponseEntity<ErrorDto> handleInternalException(Exception e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FileNotFoundException.class, EntityNotFoundException.class, UsernameNotFoundException.class,
            NullPointerException.class})
    protected ResponseEntity<ErrorDto> handleNotFoundException(Exception e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.FORBIDDEN, String.format("You don't have access", e.getMessage()));
        return new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AuthException.class, ExpiredJwtException.class,
            SignatureException.class, MalformedJwtException.class})
    protected ResponseEntity<ErrorDto> handleRestException(Exception e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        ErrorDto error = new ErrorDto(status, ex.getMessage());
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }
}
