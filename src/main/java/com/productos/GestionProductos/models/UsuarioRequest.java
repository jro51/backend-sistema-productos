package com.productos.GestionProductos.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String username;
}
