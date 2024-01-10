package com.tienda.web.app.controllers;

import java.io.IOException;
import java.util.List;
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

import com.tienda.web.app.models.entity.Brand;
import com.tienda.web.app.models.entity.Product;
import com.tienda.web.app.services.BrandService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/marca")
public class BrandController {

	@Autowired
	private BrandService service;

	@GetMapping
	public ResponseEntity<?> list() {

		return ResponseEntity.ok().body(service.finAll());
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<?> view(@PathVariable Long id) {

		Optional<Brand> currentBrand = service.finbyId(id);

		if (currentBrand.isEmpty()) {

			//return ResponseEntity.notFound().build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El marca no Existe");
		}

		return ResponseEntity.ok().body(currentBrand.get());
	}

	@PostMapping("/crear")
	public ResponseEntity<?> create(@RequestBody Brand brand) {

		Brand newBrand = service.save(brand);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newBrand);
	}

	@PutMapping("/editar/{id}")
	public ResponseEntity<?> edit(@RequestBody Brand brand, @PathVariable Long id) {

		Optional<Brand> currentBrand = service.finbyId(id);

		if (currentBrand.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Brand brandToModify = currentBrand.get();


		if (brand.getBrandName() != null) {
			brandToModify.setBrandName(brand.getBrandName());
		}

		Brand updatedBrand = service.save(brandToModify);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedBrand);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> delate(@PathVariable Long id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	/*
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * 								CONTROLADOR DE FOTO
	 *
	 */

	// Metodo en el controlador para ver la imagen
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> viewPhoto(@PathVariable Long id) {

		Optional<Brand> currentBrand = service.finbyId(id);

		if (currentBrand.isEmpty() || currentBrand.get().getPhoto() == null) {
			return ResponseEntity.notFound().build();
		}

		// Se pasa la foto "arreglo de bytes" a la respuesta por ByteArrayResource "importante Resource del .io"
		Resource image = new ByteArrayResource(currentBrand.get().getPhoto());

		// Se pasa la imagen al body
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}

	@PostMapping("/crear-con-foto") // Se debe agregar un MultiparFile con la imagen
	public ResponseEntity<?> createWithPhoto(@Valid Brand brand, BindingResult result, @RequestParam MultipartFile file)
			throws IOException {

		if (!file.isEmpty()) {
			// Si el archivo viene vacio se lo asignamos a la marca
			brand.setPhoto(file.getBytes());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(brand));
	}

	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editWithPhoto(@Valid Brand brand, BindingResult result, @PathVariable Long id,
			@RequestParam MultipartFile file) throws IOException {

		Optional<Brand> currentBrand = service.finbyId(id);

		if (currentBrand.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Brand updatedBrand = currentBrand.get();

		updatedBrand.setBrandName(brand.getBrandName());

		if (!file.isEmpty()) {
			updatedBrand.setPhoto(file.getBytes());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(updatedBrand));
	}

	/*
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * 				  CONTROLADOR PARA "ASIGNAR PRODUCTO A LA MARCA"
	 */

	// Metodo para asigar productos a la marca
	@PutMapping("/{id}/asignar-productos")// Se debe de obtener el Id de la "marca" y pasar al body un arreglo de productos(List)
	public ResponseEntity<?> assignProducts(@RequestBody List<Product> products, @PathVariable Long id) {

		// Se busca la Marca
		Optional<Brand> currentBrand = service.finbyId(id);

		if (currentBrand.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// Se asigna a otra variable
		Brand updatedBrand = currentBrand.get();

		//Iteramos la lista y agregamos con "addProducts"	
		for (Product product : products) {
			updatedBrand.addProducts(product);
		};

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(updatedBrand));
	}

	@PutMapping("/{id}/eliminar-producto") // Aqui no se pasara una lista, si no que se pasa el producto
	public ResponseEntity<?> delateProduct(@RequestBody Product product, @PathVariable("id") Long Id) {

		Optional<Brand> currentBrand = service.finbyId(Id);

		if (!currentBrand.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Brand updatedBrand = currentBrand.get();

		updatedBrand.removeProduct(product);

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(updatedBrand));
	}
}
