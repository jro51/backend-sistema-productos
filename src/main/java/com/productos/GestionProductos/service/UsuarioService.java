package com.productos.GestionProductos.service;

import com.productos.GestionProductos.entity.Usuario;
import com.productos.GestionProductos.models.UsuarioRequest;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Usuario save(Usuario usuario);
    Optional<Usuario> update(UsuarioRequest usuario, Long id);
    void deleteById(Long id);
}
