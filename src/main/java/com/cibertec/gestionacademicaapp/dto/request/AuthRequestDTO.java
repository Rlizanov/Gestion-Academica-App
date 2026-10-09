package com.cibertec.gestionacademicaapp.dto.request;
import lombok.Data;

@Data
public class AuthRequestDTO {
    private String usuario;
    private String password;
}