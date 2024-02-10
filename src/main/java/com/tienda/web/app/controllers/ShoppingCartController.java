package com.tienda.web.app.controllers;

//import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

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

import com.tienda.web.app.models.entity.ShoppingCart;
import com.tienda.web.app.models.entity.CartItem;
import com.tienda.web.app.services.ShoppingCartService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/carrito")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService service;

	@GetMapping
	public ResponseEntity<?> list() {
		return ResponseEntity.ok().body(service.finAll());
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<?> view(@PathVariable Long id) {

		Optional<ShoppingCart> currentShoppingCart = service.finById(id);

		if (!currentShoppingCart.isPresent()) {
			
			//return ResponseEntity.notFound().build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El carrito no Existe");
		}

		return ResponseEntity.ok().body(currentShoppingCart.get());
	}

	@PostMapping("/crear")
	public ResponseEntity<?> create(@RequestBody ShoppingCart shoppingCart) {

		if (shoppingCart.getUser() == null || shoppingCart.getProducts().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No hay usuario y/o productos");
		}

		shoppingCart.getItems().forEach(item -> item.setShoppingCart(shoppingCart));

		ShoppingCart newShoppingCart = service.save(shoppingCart);

		return ResponseEntity.status(HttpStatus.CREATED).body(newShoppingCart);
	}

	@PutMapping("/editar/{id}")
	public ResponseEntity<?> edit(@RequestBody ShoppingCart shoppingCart, @PathVariable Long id) {

		Optional<ShoppingCart> currentShoppingCart = service.finById(id);

		if (!currentShoppingCart.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		ShoppingCart shoppingCartToModify = currentShoppingCart.get();

		if (shoppingCart.getProducts() != null) {
			shoppingCartToModify.setProducts(shoppingCart.getProducts());
		}

		if (shoppingCart.getUser() != null) {
			shoppingCartToModify.setUser(shoppingCart.getUser());
		}

		// se crea un conjunto Set el cual contendra los items nuevos o actualizados
		Set<CartItem> updatedItems = new HashSet<>();

		// validamos que no este null y luego iteramos sobre los items de CartItem
		if (shoppingCart.getItems() != null) {
			
			// iteramos en cada elemento de CartItem sobre la coleccion de 
			for (CartItem item : shoppingCart.getItems()) {
				
				// Asigamos el item a shoppingCartToModify
				item.setShoppingCart(shoppingCartToModify);
				
				//Agregamos el item actualizado a la variable updateItems
				updatedItems.add(item);
			}
		}

		// Limpiamos la coleccion actual
		shoppingCartToModify.getItems().clear();
		
		// agregamos los items actualizamos de updatedItems
		shoppingCartToModify.getItems().addAll(updatedItems);

		ShoppingCart updatedShoppingCart = service.save(shoppingCartToModify);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedShoppingCart);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Optional<ShoppingCart> currentShoppingCart = service.finById(id);

		if (!currentShoppingCart.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		ShoppingCart shoppingCartToModify = currentShoppingCart.get();

		shoppingCartToModify.getItems().clear();
		shoppingCartToModify.setProducts(null);
		shoppingCartToModify.setUser(null);

		try {
			service.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/{id}/total")
	public ResponseEntity<?> total(@PathVariable long id) {

		Optional<ShoppingCart> currentShoppingCart = service.finById(id);

		if (!currentShoppingCart.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		ShoppingCart shoppingCart = currentShoppingCart.get();

		double total = shoppingCart.calculateTotal();

		return ResponseEntity.ok(total);
	}

	@GetMapping("/factura/{id}")
	public ResponseEntity<String> invoice(@PathVariable long id) throws IOException {

		/*
		// llamamos el metodo del servis de carrito para generar la factura
		byte[] facturaPDF = service.pagar(id);

		// se veririca si la factura no pudo ser encontrada (carrito no se encontro)
		if (facturaPDF.length == 0) {
		return ResponseEntity.notFound().build();
		}

		// se escribe, sobreescribe o genera la factura en un archivo con el nombre
		factura.pdf para visualizar
		try (FileOutputStream factura = new FileOutputStream("factura.pdf")) {
		factura.write(facturaPDF);
		}
		// retorna la factura en formato PDF
		return ResponseEntity.ok(facturaPDF);
		*/

		// Se llama al metodo creado para ver el contenido de la factura.
		String invoiceContent = service.invoiceContent(id);

		// Se valida que el contenido no este vacio
		if (invoiceContent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// retorna el contenido
		return ResponseEntity.ok(invoiceContent);
	}

}
