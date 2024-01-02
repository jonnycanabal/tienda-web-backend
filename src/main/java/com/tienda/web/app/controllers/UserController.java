package com.tienda.web.app.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.web.app.models.entity.ShoppingCart;
import com.tienda.web.app.models.entity.User;
import com.tienda.web.app.services.UserService;

/*
 * @RestController, se manejan las respuestas HTTP y las anotaciones GET,POST,PUT y DELETE a una URL determinada
 * @RequestMpping, mapea una URL especifica, las solicitud tiene que comentar con la URL especificada
 * 
*/
@RestController
@RequestMapping("/usuario")
public class UserController {

	// Importar la interfaz del Service para la interaccion y gestion de sus metodos
	@Autowired
	private UserService service;

	@GetMapping
	public ResponseEntity<?> list() {

		return ResponseEntity.ok().body(service.finAll());
	}

	// Recuerda dar una ruta a tus peticiones
	// @PathVariable, permite extraer valores de nuestra variable a la URL y
	// mapearlos al parametro del metodo
	@GetMapping("/ver/{id}")
	public ResponseEntity<?> view(@PathVariable Long id) {

		Optional<User> currentUser = service.finById(id);

		if (currentUser.isEmpty()) {
			
			//return ResponseEntity.notFound().build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario no Existe");
		}

		return ResponseEntity.ok().body(currentUser.get());
	}

	// @RequestBody, el método se debe vincular al cuerpo de la solicitud HTTP
	@PostMapping("/crear")
	public ResponseEntity<?> create(@RequestBody User user) {

		ShoppingCart cart = new ShoppingCart();
		cart.setUser(user);

		user.setShoppingCart(cart);

		User newUser = service.save(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	// Recordar, referenciar el usuario y id para asi saber cual se va editar
	@PutMapping("/editar/{id}")
	public ResponseEntity<?> edit(@RequestBody User user, @PathVariable Long id) {

		// Se busca el usuario
		Optional<User> currentUser = service.finById(id);

		if (currentUser.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// Se optiene el usuario
		User userToModify = currentUser.get();

		// Utilizamos setFirtsName() para cambiar el valor con el valor que proviene del Request (solicitud HTTP)
		if (user.getFirtsName() != null) {
			userToModify.setFirtsName(user.getFirtsName());
		}

		if (user.getMiddleName() != null) {
			userToModify.setMiddleName(user.getMiddleName());
		}

		if (user.getLastName() != null) {
			userToModify.setLastName(user.getLastName());
		}

		if (user.getSeconLastName() != null) {
			userToModify.setSeconLastName(user.getSeconLastName());
		}

		if (user.getPhoneNumber() != null) {
			userToModify.setPhoneNumber(user.getPhoneNumber());
		}

		if (user.getEmail() != null) {
			userToModify.setEmail(user.getEmail());
		}

		// persistimos o guardamos con nuestro service.save antes de pasarlo al body
		User updatedUser = service.save(userToModify);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> delate(@PathVariable Long id) {

		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
