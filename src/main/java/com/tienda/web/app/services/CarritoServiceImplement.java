package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.Carrito;
import com.tienda.web.app.models.repository.CarritoRepository;

@Service
public class CarritoServiceImplement implements CarritoService {

	@Autowired
	private CarritoRepository repository;
	
	@Override
	public Iterable<Carrito> finAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Carrito> finById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Carrito save(Carrito carrito) {	
		return repository.save(carrito);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
