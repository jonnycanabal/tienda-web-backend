package com.tienda.web.app.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.web.app.models.entity.CartItem;
import com.tienda.web.app.services.CartItemService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/itemcarrito")
public class CartItemController {

	@Autowired
	private CartItemService service;

	@GetMapping
	public ResponseEntity<?> list() {
		return ResponseEntity.ok().build();
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<?> view(@PathVariable Long id) {

		Optional<CartItem> currentCartItem = service.finById(id);

		if (!currentCartItem.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(currentCartItem.get());
	}

	@PutMapping("/crear")
	public ResponseEntity<?> create(@RequestBody CartItem cartItem) {

		CartItem newCartItem = service.save(cartItem);

		return ResponseEntity.status(HttpStatus.CREATED).body(newCartItem);
	}

	@PostMapping("/editar/{id}")
	public ResponseEntity<?> edit(@RequestBody CartItem cartItem, @PathVariable Long id) {

		Optional<CartItem> currentCartItem = service.finById(id);

		if (!currentCartItem.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		CartItem CartItemToModify = currentCartItem.get();

		if (cartItem.getShoppingCart() != null) {
			CartItemToModify.setShoppingCart(cartItem.getShoppingCart());
		}

		if (cartItem.getProduct() != null) {
			CartItemToModify.setProduct(cartItem.getProduct());
		}

		if (cartItem.getQuantity() != null) {
			CartItemToModify.setQuantity(cartItem.getQuantity());
		}

		CartItem updatedCartItem = service.save(CartItemToModify);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedCartItem);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> delate(@PathVariable Long id) {

		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
