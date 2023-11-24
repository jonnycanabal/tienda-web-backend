package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.Usuario;
import com.tienda.web.app.models.repository.UsuarioRepository;

@Service
public class UsuarioServiceImplement implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Usuario> finAll() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> finById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Usuario save(Usuario usuario) {
		
		return repository.save(usuario);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);

	}

}
