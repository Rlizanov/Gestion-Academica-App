package com.cibertec.gestionacademicaapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Atrapa los errores de validación de los DTOs (@NotBlank, @Email, @Size)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo", HttpStatus.BAD_REQUEST.value()); // 400
        respuesta.put("mensaje", "Error en los datos enviados");

        // Extraemos solo el nombre del campo y el mensaje de error limpio
        Map<String, String> detalles = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.put(error.getField(), error.getDefaultMessage());
        }
        respuesta.put("detalles", detalles);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // 2. Atrapa los errores de lógica de negocio (los "throw new RuntimeException" de tus Services)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresDeNegocio(RuntimeException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo", HttpStatus.CONFLICT.value()); // 409 Conflict o 400
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }
}