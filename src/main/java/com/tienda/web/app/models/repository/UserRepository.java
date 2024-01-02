package com.tienda.web.app.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.User;

// contrato con la interfaz "CrudRepository" de spring para las operaciones del CRUD
public interface UserRepository extends CrudRepository<User, Long> {

}
