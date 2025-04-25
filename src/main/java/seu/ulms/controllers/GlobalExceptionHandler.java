package seu.ulms.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 🔸 400 - Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 🔸 401 - Unauthorized (missing or invalid token)
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(InsufficientAuthenticationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Unauthorized");
        response.put("message", "Authentication is required to access this resource.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 🔸 403 - Forbidden (no permission)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Forbidden");
        response.put("message", "You don't have permission to perform this action.");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // 🔸 404 - Entity Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 🔸 400 - General Runtime Exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex); // ✅ Log the full stack trace
        Map<String, String> response = new HashMap<>();
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Map<String, String>> handleDuplicateKeyException(DataIntegrityViolationException ex) {
//        Map<String, String> response = new HashMap<>();
//        response.put("error", "Bad Request");
//
//        String message = ex.getMostSpecificCause().getMessage(); // تاخذ الرسالة الحقيقية من قاعدة البيانات
//
//        if (message.contains("university") && message.contains("domain")) {
//            response.put("message", "University with the same domain already exists.");
//        } else if (message.contains("users") && message.contains("email")) {
//            response.put("message", "User with the same email already exists.");
//        } else if (message.contains("users") && message.contains("user_name")) {
//            response.put("message", "Username is already taken.");
//
//        } else if (message.contains("Id") && message.contains("id")) {
//            response.put("message", "Id is already taken.");
//
//        } else {
//            response.put("message", "Duplicate key value violates a unique constraint.");
//        }
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
}