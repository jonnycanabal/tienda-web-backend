package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.ItemCarrito;

@Service
public class ItemCarritoServiceImplement implements ItemCarritoService {
	
	@Autowired
	private ItemCarritoService service;

	@Override
	public Iterable<ItemCarrito> finAll() {
		
		return service.finAll();

	}

	@Override
	public Optional<ItemCarrito> finById(Long id) {

		return service.finById(id);
	}

	@Override
	@Transactional
	public ItemCarrito save(ItemCarrito itemCarrito) {

		return service.save(itemCarrito);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {

		service.deleteById(id);
	}

}
