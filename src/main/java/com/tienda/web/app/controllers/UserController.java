package com.tienda.web.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.tienda.web.app.models.repository.ShoppingCartRepository;
import com.tienda.web.app.services.UserService;

/*
 * @RestController, se manejan las respuestas HTTP y las anotaciones GET,POST,PUT y DELETE a una URL determinada
 * @RequestMpping, mapea una URL especifica, las solicitud tiene que comentar con la URL especificada
 * 
*/
//puerto de Angular en este caso para el Cors - Esto no afecta e las pruebas den "POSTMAN" o "*" para cualquier ruta
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/usuario")
public class UserController {

	// Importar la interfaz del Service para la interaccion y gestion de sus metodos
	@Autowired
	private UserService service;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	@GetMapping
	public ResponseEntity<?> list() {

		return ResponseEntity.ok().body(service.finAll());
	}
	
//	@GetMapping("/buscar")
//	public ResponseEntity<User> findUsers(@RequestParam String firtsName,
//										@RequestParam String middleName,
//										@RequestParam String lastName,
//										@RequestParam String seconLastName){
//		
//		Optional<User> currentUser = service.findByFirtsNameOrMiddleNameOrLastNameOrSeconLastName(firtsName, middleName, lastName, seconLastName);
//		
//		return ResponseEntity.ok().body(currentUser.get());
//	}

	@PostMapping("/buscar")
	public ResponseEntity<?> findUsers(@RequestBody Map<String, String> requestParams){
		String firtsName = requestParams.get("firtsName");
		String middleName = requestParams.get("middleName");
		String lastName = requestParams.get("lastName");
		String seconLastName = requestParams.get("seconLastName");
		
		List<User> currentUser = service.findByFirtsNameContainingIgnoreCaseOrMiddleNameIgnoreCaseOrLastNameIgnoreCaseOrSeconLastNameIgnoreCase(firtsName, middleName, lastName, seconLastName);
		
		if(currentUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario no existe");
		}
		
		return ResponseEntity.ok().body(currentUser);
		
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
	public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
		
		if(result.hasFieldErrors()) {
			return validation(result);
		}

		ShoppingCart cart = new ShoppingCart();
		cart.setUser(user);

		user.setShoppingCart(cart);

		user.setEnabled(true);
		
		user.setAdmin(false);
		
		User newUser = service.save(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	//Metodo duplicado con el cual se podra crear un admin, mientras que el create solo "ROLE_USER"
	@PostMapping("/crear/admin")
	public ResponseEntity<?> createAdmin(@Valid @RequestBody User user, BindingResult result) {
		
		if(result.hasFieldErrors()) {
			return validation(result);
		}

		ShoppingCart cart = new ShoppingCart();
		cart.setUser(user);

		user.setShoppingCart(cart);

		user.setEnabled(true);
		
		User newUser = service.save(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	//Metodo para validation en el "create"
	private ResponseEntity<?> validation(BindingResult result) {
		Map<String, String> errors = new HashMap<>();
		
		result.getFieldErrors().forEach(err -> {
			errors.put(err.getField(),"El campo" + err.getField() + " " + err.getDefaultMessage());
		});
		
		return ResponseEntity.badRequest().body(errors);
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
		
		if(user.getUsername() != null) {
			userToModify.setUsername(user.getUsername());
		}
		
		if(user.getPassword() != null) {
			userToModify.setPassword(user.getPassword());
		}
		
		if(user.isEnabled() != userToModify.isEnabled()) {
			userToModify.setEnabled(user.isEnabled());
		}
		
		if(user.isAdmin() != userToModify.isAdmin()) {
			userToModify.setAdmin(user.isAdmin());
		}

		// persistimos o guardamos con nuestro service.save antes de pasarlo al body
		User updatedUser = service.save(userToModify);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> delate(@PathVariable Long id) {

		Optional<User> currentUser = service.finById(id);
		
		if(currentUser.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//Elimina el carrito para romper la relacion
		
		if(currentUser.get().getShoppingCart() != null) {
			// Obtener el ID del carrito asociado al usuario
			Long idShoppingCart = currentUser.get().getShoppingCart().getId();
			
			try {
				shoppingCartRepository.deleteById(idShoppingCart);
			} catch (EmptyResultDataAccessException  e) {

				System.out.println("El carrito de compras con ID " + idShoppingCart + " no existe!");
			}
		}
		
		try {
			//Elimina el Usuario
			service.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.ok().body("El usuario " + currentUser.get().getFirtsName() + " Se elimino correctamente!");
		}
		
	}

}
