package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.ItemCarrito;

public interface ItemCarritoService {
	
	public Iterable<ItemCarrito> finAll();
	
	public Optional<ItemCarrito> finById(Long id);
	
	public ItemCarrito save (ItemCarrito itemCarrito);
	
	public void deleteById (Long id);

}
