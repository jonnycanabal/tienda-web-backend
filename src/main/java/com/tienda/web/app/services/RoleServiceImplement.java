package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.web.app.models.entity.Role;
import com.tienda.web.app.models.repository.RoleRepository;

@Service
public class RoleServiceImplement implements RoleService {
	
	@Autowired
	private RoleRepository repository;

	@Override
	public Iterable<Role> finAll() {

		return repository.findAll();
	}

	//@Override
	//public Optional<Role> finById(Long id) {

	//	return repository.findById(id);
	//}

	public Optional<Role> findByName(String name) {
		
		return repository.findByName(name);
	}
	
	@Override
	public Role save(Role role) {

		return repository.save(role);
	}

	@Override
	public void delateById(Long id) {

		repository.deleteById(id);
	}

}
