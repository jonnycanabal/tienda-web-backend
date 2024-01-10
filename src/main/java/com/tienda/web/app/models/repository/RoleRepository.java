package com.tienda.web.app.models.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(String name);
		
}
