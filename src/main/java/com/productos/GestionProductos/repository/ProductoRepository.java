package com.productos.GestionProductos.repository;

import com.productos.GestionProductos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
