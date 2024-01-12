package com.tienda.web.app.services;

import java.util.List;
import java.util.Optional;

import com.tienda.web.app.models.entity.Product;

public interface ProductService {

	public Iterable<Product> finAll();

	public Optional<Product> finById(Long id);

	public Product save(Product product);

	public void deleteById(Long id);
	
	public List<Product> findByProductNameContainingIgnoreCase (String productName);

}
