package com.tienda.web.app.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tienda.web.app.models.entity.Producto;
import com.tienda.web.app.services.ProductoService;

import javax.validation.Valid;

@RestController
@RequestMapping("/producto")
public class ProductoController {

	@Autowired
	private ProductoService service;

	@GetMapping
	public ResponseEntity<?> listar() {

		return ResponseEntity.ok().body(service.finAll());
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Producto> productoActual = service.finById(id);

		if (productoActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(productoActual.get());
	}

	@PostMapping("/crear")
	public ResponseEntity<?> crear(@RequestBody Producto producto) {

		Producto nuevoProducto = service.save(producto);

		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
	}

	@PutMapping("/editar/{id}")
	public ResponseEntity<?> editar(@RequestBody Producto producto, @PathVariable Long id) {

		Optional<Producto> productoActual = service.finById(id);

		if (productoActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Producto productoModificado = productoActual.get();

		if (producto.getNombre() != null) {
			productoModificado.setNombre(producto.getNombre());
		}

		if (producto.getCantidad() != null) {
			productoModificado.setCantidad(producto.getCantidad());
		}

		if (producto.getPrecio() != null) {
			productoModificado.setPrecio(producto.getPrecio());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productoModificado));
	}

	@DeleteMapping("/elimiar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	/*
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * - - - - - - - - - - - - - - - - -
	 */
	/* Controlador de foto */

	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id) {

		Optional<Producto> productoActual = service.finById(id);

		if (productoActual.isEmpty() || productoActual.get().getFoto() == null) {
			return ResponseEntity.notFound().build();
		}

		Resource imagen = new ByteArrayResource(productoActual.get().getFoto());

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}

	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> crearConFoto(@Valid Producto producto, BindingResult result,
			@RequestParam MultipartFile archivo) throws IOException {

		if (!archivo.isEmpty()) {
			producto.setFoto(archivo.getBytes());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(producto));
	}

	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editarConFoto(@Valid Producto producto, BindingResult result, @PathVariable Long id,
			@RequestParam MultipartFile archivo) throws IOException {

		Optional<Producto> productoActual = service.finById(id);

		if (productoActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Producto productoModificado = productoActual.get();

		if (producto.getNombre() != null) {
			productoModificado.setNombre(producto.getNombre());
		}

		if (producto.getCantidad() != null) {
			productoModificado.setCantidad(producto.getCantidad());
		}

		if (producto.getPrecio() != null) {
			productoModificado.setPrecio(producto.getPrecio());
		}

		if (!archivo.isEmpty()) {
			productoModificado.setFoto(archivo.getBytes());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productoModificado));
	}
}
