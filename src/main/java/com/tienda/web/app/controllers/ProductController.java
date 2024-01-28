package com.tienda.web.app.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tienda.web.app.models.entity.Product;
import com.tienda.web.app.services.ProductService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/producto")
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping
	public ResponseEntity<?> list() {

		return ResponseEntity.ok().body(service.finAll());
	}
	
	@PostMapping("/buscar")
	public ResponseEntity<?> findProduct(@RequestBody Map<String, String> requestParams){
		String productName = requestParams.get("productName");
		
		List<Product> currentProduct = service.findByProductNameContainingIgnoreCase(productName);
		
		if(currentProduct.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe!");
		}
		
		return ResponseEntity.ok(currentProduct);
		
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<?> view(@PathVariable Long id) {

		Optional<Product> currentProduct = service.finById(id);

		if (!currentProduct.isPresent()) {
			
			//return ResponseEntity.notFound().build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no Existe");
		}

		return ResponseEntity.ok().body(currentProduct.get());
	}

	@PostMapping("/crear")
	public ResponseEntity<?> create(@RequestBody Product product) {

		Product newProduct = service.save(product);

		return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
	}

	@PutMapping("/editar/{id}")
	public ResponseEntity<?> edit(@RequestBody Product product, @PathVariable Long id) {

		Optional<Product> currentProduct = service.finById(id);

		if (!currentProduct.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Product productToModify = currentProduct.get();

		if (product.getProductName() != null) {
			productToModify.setProductName(product.getProductName());
		}

		if (product.getPrice() != null) {
			productToModify.setPrice(product.getPrice());
		}

		Product uptatedProduct = service.save(productToModify);

		return ResponseEntity.status(HttpStatus.CREATED).body(uptatedProduct);
	}

	@DeleteMapping("/elimiar/{id}")
	public ResponseEntity<?> delate(@PathVariable Long id) {

		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	/*
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * 								CONTROLADOR DE FOTO
	 */

	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> viewPhoto(@PathVariable Long id) {

		Optional<Product> currentProduct = service.finById(id);

		if (!currentProduct.isPresent() || currentProduct.get().getPhoto() == null) {
			return ResponseEntity.notFound().build();
		}

		Resource image = new ByteArrayResource(currentProduct.get().getPhoto());

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}

	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> createWithPhoto(@Valid Product product, BindingResult result,
			@RequestParam MultipartFile file) throws IOException {

		if (!file.isEmpty()) {
			product.setPhoto(file.getBytes());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
	}

	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editWithPhoto(@Valid Product product, BindingResult result, @PathVariable Long id,
			@RequestParam MultipartFile file) throws IOException {

		Optional<Product> currentProduct = service.finById(id);

		if (!currentProduct.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Product productToModify = currentProduct.get();

		if (product.getProductName() != null) {
			productToModify.setProductName(product.getProductName());
		}

		if (product.getPrice() != null) {
			productToModify.setPrice(product.getPrice());
		}

		if (!file.isEmpty()) {
			productToModify.setPhoto(file.getBytes());
		}

		Product updatedProduct = service.save(productToModify);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
	}
}
