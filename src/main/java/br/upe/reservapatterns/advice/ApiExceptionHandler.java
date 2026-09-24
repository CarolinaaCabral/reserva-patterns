package br.upe.reservapatterns.advice;

import br.upe.reservapatterns.dto.ApiError;
import br.upe.reservapatterns.exception.ConflictException;
import br.upe.reservapatterns.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(NotFoundException.class)
  ResponseEntity<ApiError> missing(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(ex.getMessage()));
  }

  @ExceptionHandler(ConflictException.class)
  ResponseEntity<ApiError> conflict(ConflictException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(ex.getMessage()));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<ApiError> duplicate(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("Conflito de dados"));
  }

  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<ApiError> denied(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError("Acesso negado"));
  }

  @ExceptionHandler(AuthenticationException.class)
  ResponseEntity<ApiError> unauthenticated(AuthenticationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ApiError("Credenciais inválidas"));
  }

  @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class,
          HttpMessageNotReadableException.class})
  ResponseEntity<ApiError> invalid(Exception ex) {
    return ResponseEntity.badRequest().body(new ApiError("Requisição inválida"));
  }
}
