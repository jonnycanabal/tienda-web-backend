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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.web.app.models.entity.Carrito;
import com.tienda.web.app.models.entity.ItemCarrito;
import com.tienda.web.app.services.CarritoService;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

	@Autowired
	private CarritoService service;

	@GetMapping
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok().body(service.finAll());
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Carrito> carritoActual = service.finById(id);

		if (carritoActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(carritoActual.get());
	}

	@PostMapping("/crear")
	public ResponseEntity<?> crear(@RequestBody Carrito carrito) {

		if (carrito.getUsuario() == null || carrito.getProductos().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No hay usuario y/o productos");
		}

		carrito.getItems().forEach(item -> item.setCarrito(carrito));

		Carrito carritoNuevo = service.save(carrito);

		return ResponseEntity.status(HttpStatus.CREATED).body(carritoNuevo);
	}

	@PutMapping("/editar/{id}")
	public ResponseEntity<?> editar(@RequestBody Carrito carrito, @PathVariable Long id) {

		Optional<Carrito> carritoActual = service.finById(id);

		if (carritoActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Carrito carritoModificado = carritoActual.get();

		if (carrito.getProductos() != null) {
			carritoModificado.setProductos(carrito.getProductos());
		}

		if (carrito.getUsuario() != null) {
			carritoModificado.setUsuario(carrito.getUsuario());
		}

		// se crea un conjunto Set el cual contendra los items nuevos o actualizados
		Set<ItemCarrito> itemsActualizados = new HashSet<>();

		// validamos que no este null y luego iteramos sobre los items de ItemCarrito
		if (carrito.getItems() != null) {
			for (ItemCarrito item : carrito.getItems()) {
				// luego asociamos lo de ItemCarrito con el Carrito Actual en este caso lo que
				// ya se tenia en carritodb a itemsactualizados
				item.setCarrito(carritoModificado);
				itemsActualizados.add(item);
			}
		}

		// luego limpiamos la coleccion actual de ItemCarrito de carritodb y despues
		// agregamos los items actualizamos de itemsActualizados
		carritoModificado.getItems().clear();
		carritoModificado.getItems().addAll(itemsActualizados);

		Carrito carritoActualizado = service.save(carritoModificado);

		return ResponseEntity.status(HttpStatus.CREATED).body(carritoActualizado);
	}

	@DeleteMapping("eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {

		Optional<Carrito> carritoActual = service.finById(id);

		if (carritoActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Carrito carritodb = carritoActual.get();

		carritodb.getItems().clear();
		carritodb.setProductos(null);
		carritodb.setUsuario(null);

		try {
			service.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/{id}/total")
	public ResponseEntity<?> total(@PathVariable long id) {

		Optional<Carrito> carritoActual = service.finById(id);

		if (carritoActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Carrito carrito = carritoActual.get();

		double total = carrito.total();

		return ResponseEntity.ok(total);
	}

	@GetMapping("/factura/{id}")
	public ResponseEntity<String> generarFactura(@PathVariable long id) throws IOException {

		// llamamos el metodo del servis de carrito para generar la factura
		// byte[] facturaPDF = service.pagar(id);

		// se veririca si la factura no pudo ser encontrada (carrito no se encontro)
		// if (facturaPDF.length == 0) {
		// return ResponseEntity.notFound().build();
		// }

		// se escribe, sobreescribe o genera la factura en un archivo con el nombre
		// factura.pdf para visualizar
		// try (FileOutputStream factura = new FileOutputStream("factura.pdf")) {
		// factura.write(facturaPDF);
		// }
		// retorna la factura en formato PDF
		// return ResponseEntity.ok(facturaPDF);

		// Se llama al metodo creado para ver el contenido de la factura.
		String contenidoFactura = service.contenidoFactura(id);

		// Se valida que el contenido no este vacio
		if (contenidoFactura.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// retorna el contenido
		return ResponseEntity.ok(contenidoFactura);
	}

}
