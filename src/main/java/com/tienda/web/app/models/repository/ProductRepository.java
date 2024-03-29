package com.tienda.web.app.models.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	
	List<Product> findByProductNameContainingIgnoreCase(String productName);

}
