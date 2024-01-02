package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.Product;
import com.tienda.web.app.models.repository.ProductRepository;

@Service
public class ProductServiceImplement implements ProductService {

	@Autowired
	private ProductRepository repository;

	@Override
	public Iterable<Product> finAll() {

		return repository.findAll();
	}

	@Override
	public Optional<Product> finById(Long id) {

		return repository.findById(id);
	}

	@Override
	@Transactional
	public Product save(Product product) {

		return repository.save(product);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {

		repository.deleteById(id);
	}

}
