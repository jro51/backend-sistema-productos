package com.productos.GestionProductos.service;

import com.productos.GestionProductos.entity.Usuario;
import com.productos.GestionProductos.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public JpaUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

//    Lo que hace este metodo es primero validar que el username del usuario exista. Si no existe
//    entonces lanzará un error con el mensaje escrito. Ahora, primero crearemos un objeto del tipo Usuario
//    de nuestra entidad para obtener los datos de la bd. Segundo vamos a crear una lista del tipo
//    GrantedAuthority donde convertiremos los datos de los roles a SimpleGrantedAuthority porque lo
//    necesitaremos para colocar como argumento del objeto a retornar que en este caso es User (propio de
//    Spring Security).
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        if(usuarioOptional.isEmpty()){
            throw new UsernameNotFoundException("El usuario no ha sido encontrado");
        }
        Usuario usuarioBd = usuarioOptional.get();
        List<GrantedAuthority> authorities = usuarioBd.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .collect(Collectors.toList());
        return new User(
                username,
                usuarioBd.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }
}
