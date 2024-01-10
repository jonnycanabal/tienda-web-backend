package com.tienda.web.app.services;

import com.tienda.web.app.models.entity.Role;

public interface RoleService {

	public Iterable<Role> finAll();
	
	//public Optional<Role> finById(Long id);
	
	public Role save(Role role);
	
	public void delateById(Long id);
}
