package com.tienda.web.app.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

}
