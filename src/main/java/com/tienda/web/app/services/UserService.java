package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.User;

// Interfaz para definir la estructura de los metodos para nuestra gention de User
// Aqui se realiza por decirlo asi un contrato el cual despues en una clase realizaremos la respectiva implementacion
public interface UserService {

	// ** Se define el que, pero no el como **

	/*
	 * "Iterable", nos permite iterar sobre una coleccion de elementos y usar
	 * estructuras de control de bucle (for-each)
	 */
	public Iterable<User> finAll();
	
	/*
	 * "Optional", forma clara y segura de representar un valor null, con esto un
	 * valor podria o no existir y manejarlo de forma diferentes con un get y un
	 * respectivo mensaje de la ausencia de algun dato.
	 */
	public Optional<User> finById(Long id);

	/*
	 * retorna el objeto de tipo User, recibe User y lo guarda
	 */
	public User save(User user);

	/* El void no retorna nada y elimina por el id */
	public void deleteById(Long id);
	
	//validar si existe el User con el username
	boolean existsByUsername(String username);
}
