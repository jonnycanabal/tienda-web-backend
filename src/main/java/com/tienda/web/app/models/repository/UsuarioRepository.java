package com.tienda.web.app.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

}
