package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.User;
import com.tienda.web.app.models.repository.UserRepository;

//Implementamos la Interfaz "service" y se define el como de los metodos segun nuestras necesidades

/*
 * @service registra esta clase como un componente de spring, 
 * permite la inyección de dependencias y la gestión centralizada,
 * puede ser inyectada en los controladores con @Autowired
 */

@Service
public class UserServiceImplement implements UserService {

	// Inyectamos el repository con @Autowired
	// asi se puede utilizarlo y realizar los registros
	@Autowired
	private UserRepository repository;

	@Override
	public Iterable<User> finAll() {

		return repository.findAll();
	}

	@Override
	public Optional<User> finById(Long id) {

		return repository.findById(id);
	}

	@Override
	@Transactional
	public User save(User user) {

		return repository.save(user);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {

		repository.deleteById(id);

	}

}
