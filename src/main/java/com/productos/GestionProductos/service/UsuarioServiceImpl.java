package com.productos.GestionProductos.service;

import com.productos.GestionProductos.entity.Usuario;
import com.productos.GestionProductos.models.UsuarioRequest;
import com.productos.GestionProductos.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final PasswordEncoder passwordEncoder;

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> update(UsuarioRequest usuario, Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if(usuarioOptional.isPresent()) {
            Usuario usuarioBd = usuarioOptional.get();
            usuarioBd.setNombre(usuario.getNombre());
            usuarioBd.setApellido(usuario.getApellido());
            usuarioBd.setEmail(usuario.getEmail());
            usuarioBd.setUsername(usuario.getUsername());
            return Optional.of(usuarioRepository.save(usuarioBd));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}
