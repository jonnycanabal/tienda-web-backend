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

import com.tienda.web.app.models.entity.ItemCarrito;
import com.tienda.web.app.services.ItemCarritoService;

@RestController
@RequestMapping("/itemcarrito")
public class ItemCarritoController {
	
	@Autowired
	private ItemCarritoService service;
	
	@GetMapping
	public ResponseEntity<?> listar(){
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id){
		
		Optional<ItemCarrito> o = service.finById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(o.get());
	}
	
	@PutMapping("/crear")
	public ResponseEntity<?> crear(@RequestBody ItemCarrito itemCarrito){
		
		ItemCarrito itemCarritoDb= service.save(itemCarrito);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(itemCarritoDb);
	}
	
	@PostMapping("/editar/{id}")
	public ResponseEntity<?> editar(@RequestBody ItemCarrito itemCarrito, @PathVariable Long id){
		
		Optional<ItemCarrito> o = service.finById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		ItemCarrito itemCarritoDb = o.get();
		
		itemCarritoDb.setCarrito(itemCarrito.getCarrito());
		itemCarritoDb.setProducto(itemCarrito.getProducto());
		itemCarritoDb.setCantidad(itemCarrito.getCantidad());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(itemCarrito));
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		service.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}

}
