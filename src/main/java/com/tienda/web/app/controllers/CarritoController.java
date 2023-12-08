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
import com.tienda.web.app.services.CarritoService;

@RestController
@RequestMapping("/carrito")
public class CarritoController {
	
	@Autowired
	private CarritoService service; 

	@GetMapping
	public ResponseEntity<?> listar(){
		return ResponseEntity.ok().body(service.finAll());
	}
	
	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id){
		
		Optional<Carrito> o = service.finById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(o.get());
	}
	
	@PostMapping("/crear")
	public ResponseEntity<?> crear(@RequestBody Carrito carrito){
		
		if(carrito.getUsuario() == null || carrito.getProductos().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No hay usuario y/o productos");
		}
		
		carrito.getItems().forEach(item -> item.setCarrito(carrito));
		
		Carrito carritoNuevo = service.save(carrito);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(carritoNuevo);
	}
	
	@PutMapping("/editar/{id}")
	public ResponseEntity<?> editar(@RequestBody Carrito carrito, @PathVariable Long id){
		
		Optional<Carrito> o = service.finById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Carrito carritodb = o.get();
		
		carritodb.setProductos(carrito.getProductos());
		
		if(carrito.getUsuario() != null) {
			carritodb.setUsuario(carrito.getUsuario());
		}else {
			carritodb.setUsuario(null);
		}
		
		Carrito carritoActualizado = service.save(carritodb);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(carritoActualizado));
	}
	
	@DeleteMapping("eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		
		service.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
