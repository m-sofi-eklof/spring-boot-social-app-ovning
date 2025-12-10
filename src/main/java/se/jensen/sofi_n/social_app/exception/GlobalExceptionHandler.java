package se.jensen.sofi_n.social_app.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

//hanterare för undantag från alla REST-controllers i projektet
@RestControllerAdvice //annotation: hanterare som gäller alla controllers i projektet
public class GlobalExceptionHandler {

    //metod som hanterar MehtodArgumentNotValidException undantag
    //triggas vid misslyckad validering av metodargument
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        //lagra fältnamn och valideringsfelmeddelande i hashmap
        Map<String, String> errors = new HashMap<>();

        //loopa igenom feilderros från exeption-objekts bindningResult
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            String fieldName = fieldError.getField(); //hämta ej validerat fältnamn
            String message = fieldError.getDefaultMessage(); //hämta felmeddelande
            errors.put(fieldName, message); //lägg till i hashmap
        }

        //returnera status 400 Bad Request med hashmapen som body
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
