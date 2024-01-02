package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.CartItem;

@Service
public class CartItemServiceImplement implements CartItemService {

	@Autowired
	private CartItemService service;

	@Override
	public Iterable<CartItem> finAll() {

		return service.finAll();

	}

	@Override
	public Optional<CartItem> finById(Long id) {

		return service.finById(id);
	}

	@Override
	@Transactional
	public CartItem save(CartItem cartItem) {

		return service.save(cartItem);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {

		service.deleteById(id);
	}

}
