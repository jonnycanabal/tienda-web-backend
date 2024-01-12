package com.tienda.web.app.models.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.User;

// contrato con la interfaz "CrudRepository" de spring para las operaciones del CRUD
public interface UserRepository extends CrudRepository<User, Long> {
	
	boolean existsByUsername(String username);
	
	//consulta personalidad para buscar por nombre
	Optional<User> findByUsername(String username);
	
	List<User> findByFirtsNameContainingIgnoreCaseOrMiddleNameIgnoreCaseOrLastNameIgnoreCaseOrSeconLastNameIgnoreCase(
			String firtsName, String middleName, String lastName, String seconLastName);

}
