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

import com.tienda.web.app.models.entity.Usuario;
import com.tienda.web.app.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
	@GetMapping
	public ResponseEntity<?> listar(){
		
		return ResponseEntity.ok().body(service.finAll());
	}
	
	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id){
		
		Optional<Usuario> o = service.finById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(o.get());
	}
	
	@PostMapping("/crear")
	public ResponseEntity<?> crear(@RequestBody Usuario usuario){
		
		Usuario usuariodb = service.save(usuario);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(usuariodb);
	}
	
	@PutMapping("/editar/{id}")
	public ResponseEntity<?> editar(@RequestBody Usuario usuario, @PathVariable Long id){
		
		Optional<Usuario> o = service.finById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Usuario usuariodb = o.get();
		
		usuariodb.setPrimerNombre(usuario.getPrimerNombre());
		usuariodb.setSegundoNombre(usuario.getSegundoNombre());
		usuariodb.setPrimerApellido(usuario.getPrimerApellido());
		usuariodb.setSegundoApellido(usuario.getSegundoApellido());
		usuariodb.setCelular(usuario.getCelular());
		usuariodb.setCorreo(usuario.getCorreo());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuariodb));
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		service.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}

}
