package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.CartItem;

public interface CartItemService {

	public Iterable<CartItem> finAll();

	public Optional<CartItem> finById(Long id);

	public CartItem save(CartItem cartItem);

	public void deleteById(Long id);

}
