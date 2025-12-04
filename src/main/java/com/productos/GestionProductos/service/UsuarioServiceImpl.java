package com.productos.GestionProductos.service;

import com.productos.GestionProductos.entity.Role;
import com.productos.GestionProductos.entity.Usuario;
import com.productos.GestionProductos.models.UsuarioRequest;
import com.productos.GestionProductos.repository.RoleRepository;
import com.productos.GestionProductos.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final PasswordEncoder passwordEncoder;

    private final UsuarioRepository usuarioRepository;

    private final RoleRepository roleRepository;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
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
    @Transactional
    public Usuario save(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asignar rol por defecto si no tiene roles
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            Role roleUser = roleRepository.findByNombre("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role USER no encontrado en la base de datos"));

            List<Role> roles = new ArrayList<>();
            roles.add(roleUser);
            usuario.setRoles(roles);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
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
