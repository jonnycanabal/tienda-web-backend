package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.Producto;

public interface ProductoService {

	public Iterable<Producto> finAll();

	public Optional<Producto> finById(Long id);

	public Producto save(Producto producto);

	public void deleteById(Long id);

}
