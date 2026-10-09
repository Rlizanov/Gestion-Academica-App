package com.cibertec.gestionacademicaapp.dto.response;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AuthResponseDTO {
    private String token;
    private String rol;
}