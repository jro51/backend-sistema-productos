package com.productos.GestionProductos.controller;

import com.productos.GestionProductos.entity.Producto;
import com.productos.GestionProductos.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(productoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> findById(@PathVariable Long id){
        Optional<Producto> productoOptional = productoService.findById(id);
        if(productoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.FOUND).body(productoOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto){
        productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@RequestBody Producto producto, @PathVariable Long id){
        Optional<Producto> productoOptional = productoService.findById(id);
        if(productoOptional.isPresent()){
            Producto productoActualizar = productoOptional.get();
            productoActualizar.setNombre(producto.getNombre());
            productoActualizar.setDescripcion(producto.getDescripcion());
            productoActualizar.setCantidad(producto.getCantidad());
            productoActualizar.setPrecio(producto.getPrecio());
            productoService.save(productoActualizar);
            return ResponseEntity.status(HttpStatus.OK).body(productoActualizar);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        Optional<Producto> productoOptional = productoService.findById(id);
        if(productoOptional.isPresent()){
            productoService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
