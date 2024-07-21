package com.ananda.forohub.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler (EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e){
        // Mensaje de excepcion
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(ValidationDeDatosIngresados.class)
    public ResponseEntity<String> handleValidationDeTopicos(ValidationDeDatosIngresados ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ValidationDeRoles.class)
    public ResponseEntity<String> handleValidationDeRoles(ValidationDeRoles ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // DTO para escoger lo importante del reporte stacktrace de errores
    private record DatosErrorValidacion (String campo, String error){

        // Constructor
        public DatosErrorValidacion (FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
