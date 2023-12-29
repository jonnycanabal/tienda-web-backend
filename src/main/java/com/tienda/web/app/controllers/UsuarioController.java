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

import com.tienda.web.app.models.entity.Carrito;
import com.tienda.web.app.models.entity.Usuario;
import com.tienda.web.app.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@GetMapping
	public ResponseEntity<?> listar() {

		return ResponseEntity.ok().body(service.finAll());
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Usuario> usuarioActual = service.finById(id);

		if (usuarioActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(usuarioActual.get());
	}

	@PostMapping("/crear")
	public ResponseEntity<?> crear(@RequestBody Usuario usuario) {

		Carrito carrito = new Carrito();
		carrito.setUsuario(usuario);

		usuario.setCarrito(carrito);

		Usuario nuevoUsuario = service.save(usuario);

		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
	}

	@PutMapping("/editar/{id}")
	public ResponseEntity<?> editar(@RequestBody Usuario usuario, @PathVariable Long id) {

		Optional<Usuario> usuarioActual = service.finById(id);

		if (usuarioActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Usuario usuarioModificado = usuarioActual.get();

		if (usuario.getPrimerNombre() != null) {
			usuarioModificado.setPrimerNombre(usuario.getPrimerNombre());
		}

		if (usuario.getSegundoNombre() != null) {
			usuarioModificado.setSegundoNombre(usuario.getSegundoNombre());
		}

		if (usuario.getPrimerApellido() != null) {
			usuarioModificado.setPrimerApellido(usuario.getPrimerApellido());
		}

		if (usuario.getSegundoApellido() != null) {
			usuarioModificado.setSegundoApellido(usuario.getSegundoApellido());
		}

		if (usuario.getCelular() != null) {
			usuarioModificado.setCelular(usuario.getCelular());
		}

		if (usuario.getCorreo() != null) {
			usuarioModificado.setCorreo(usuario.getCorreo());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioModificado));
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
