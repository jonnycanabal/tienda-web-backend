package com.tienda.web.app.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
