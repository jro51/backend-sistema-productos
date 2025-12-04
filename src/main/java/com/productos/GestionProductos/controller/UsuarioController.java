package com.productos.GestionProductos.controller;

import com.productos.GestionProductos.entity.Usuario;
import com.productos.GestionProductos.models.UsuarioRequest;
import com.productos.GestionProductos.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if(usuarioOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.FOUND).body(usuarioOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@RequestBody UsuarioRequest usuario, @PathVariable Long id){
        Optional<Usuario> usuarioOptional = usuarioService.update(usuario, id);
        if(usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if(usuarioOptional.isPresent()){
            usuarioService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
