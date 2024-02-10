package com.tienda.web.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.Role;
import com.tienda.web.app.models.entity.User;
import com.tienda.web.app.models.repository.RoleRepository;
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
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Iterable<User> finAll() {

		return repository.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {

		return repository.findById(id);
	}

	@Override
	@Transactional
	public User save(User user) {
		
		Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
		
		List<Role> roles = new ArrayList<>();
		
		//si esta presente se agrega el rol de usuario
		optionalRoleUser.ifPresent(role -> roles.add(role));
		
		//si is admin es true se le va asignar "ROLE_ADMIN"
		if(user.isAdmin()) {
			
			Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
			
//			roles.clear();
			
			optionalRoleAdmin.ifPresent(roles::add);
		}

		//pasamos los roles al usuario
		user.setRoles(roles);
		
		//aqui decodificamos el password que viene del request
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repository.save(user);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {

		repository.deleteById(id);

	}

	@Override
	@Transactional
	public boolean existsByUsername(String username) {

		return repository.existsByUsername(username);
	}

	@Override
	public List<User> findByFirtsNameContainingIgnoreCaseOrMiddleNameIgnoreCaseOrLastNameIgnoreCaseOrSeconLastNameIgnoreCase(String firtsName, String middleName,
			String lastName, String seconLastName) {
		
		return repository.findByFirtsNameContainingIgnoreCaseOrMiddleNameIgnoreCaseOrLastNameIgnoreCaseOrSeconLastNameIgnoreCase(firtsName, middleName, lastName, seconLastName);
	}

}
