package com.tienda.web.app.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.Producto;

public interface ProductoRepository extends CrudRepository<Producto, Long> {

}
