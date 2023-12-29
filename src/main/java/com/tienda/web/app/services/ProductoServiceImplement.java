package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.Producto;
import com.tienda.web.app.models.repository.ProductoRepository;

@Service
public class ProductoServiceImplement implements ProductoService {

	@Autowired
	private ProductoRepository repository;

	@Override
	public Iterable<Producto> finAll() {

		return repository.findAll();
	}

	@Override
	public Optional<Producto> finById(Long id) {

		return repository.findById(id);
	}

	@Override
	@Transactional
	public Producto save(Producto producto) {

		return repository.save(producto);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {

		repository.deleteById(id);
	}

}
