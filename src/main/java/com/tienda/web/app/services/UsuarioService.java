package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.Usuario;

public interface UsuarioService {

	public Iterable<Usuario> finAll();
	
	public Optional<Usuario> finById(Long id);
	
	public Usuario save(Usuario usuario);
	
	public void deleteById(Long id);
}
