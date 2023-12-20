package com.tienda.web.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.Marca;
import com.tienda.web.app.models.repository.MarcaRepository;

//Aqui se implementa la interfaz que se creo y los metodos los cuales se deberan acomodar a nuestras necesidades

@Service // con esta clase registramos nuestro componene en nuestro contenedor de spring
			// y podemos inyectarlos en nuestro controladores con autowired
public class MarcaServiceImplement implements MarcaService {

	// Aqui se inyecta nuestro repository, se importa con Autowired. Con esto ya
	// podemos utilizarlo y realizar los registros.
	@Autowired
	private MarcaRepository repository;

	@Override
	public Iterable<Marca> finAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Marca> finbyId(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Marca save(Marca marca) {
		return repository.save(marca);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
