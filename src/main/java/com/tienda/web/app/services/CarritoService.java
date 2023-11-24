package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.Carrito;

public interface CarritoService {
	
	public Iterable<Carrito> finAll();
	
	public Optional<Carrito> finById(Long id);
	
	public Carrito save (Carrito carrito);
	
	public void deleteById(Long id);
}
