package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.Role;

public interface RoleService {

	public Iterable<Role> finAll();
	
	public Optional<Role> findById(Long id);
	
	public Role save(Role role);
	
	public void delateById(Long id);
	
	Optional<Role> findByName(String name);

}
