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

import com.tienda.web.app.models.entity.Marca;
import com.tienda.web.app.models.entity.Producto;
import com.tienda.web.app.services.MarcaService;

import javax.validation.Valid;

/*@RestController, devuelve los datos en formato Json y sirve para manejar respuesta HTTP y 
tambien hacer las anotacion con los get, post, put, delet a una url determinada.*/
@RestController
@RequestMapping("/marca")
public class MarcaController {

	// Recordar importar la interfaz ya que es un tipo generico el cual se crea. ya
	// que va ser la misma y comun.
	@Autowired
	private MarcaService service;

	// dar una ruta Url a la pedicion que se va a dar en este caso Get.
	@GetMapping
	public ResponseEntity<?> listar() {

		// se pasa al cuerpo (body)de la respuseta una linda de entity.
		return ResponseEntity.ok().body(service.finAll());
	}

	@GetMapping("/ver/{id}")
	// El PathVariable se usa para extraer valores de variables a nuestra URL y
	// mapearlos al parametro del metodo.
	public ResponseEntity<?> ver(@PathVariable Long id) {

		// tenemos que buscar nuestro objeto y validaremos que exista en nuestra base de
		// datos
		Optional<Marca> marcaActual = service.finbyId(id);

		if (marcaActual.isEmpty()) {
			// el build contruye la respuesta sin contenido
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(marcaActual.get());
	}

	@PostMapping("/crear")
	// se utiliza el RequestBody ya que necesitamos agregar la Marca la cual viene
	// del Request, del cuerpo (body)
	public ResponseEntity<?> crear(@RequestBody Marca marca) {
		// Se almacena el alumno creado en una variable la cual se pasara al cuerpo de
		// la respuesta.
		Marca nuevaMarca = service.save(marca);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMarca);
	}

	@PutMapping("/editar/{id}")
	// se agregar la marca y el id para asi referencia lo que se va a editar.
	public ResponseEntity<?> editar(@RequestBody Marca marca, @PathVariable Long id) {
		// primero buscamos la marca en la Base de Datos
		Optional<Marca> marcaActual = service.finbyId(id);

		if (marcaActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// aqui optenemos nuestra marca
		Marca marcaModificada = marcaActual.get();

		// luego con nuestro metodo set() cambiamos el valor y el metodo get para
		// obtener el valor del Request.
		if (marca.getNombre() != null) {
			marcaModificada.setNombre(marca.getNombre());
		}

		// primero debemos persistir o guardalo antes de pasarlo al boty con el
		// service.save
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(marcaModificada));
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	/*
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * - - - - - - - - - - - - - - - - -
	 */
	/* Controlador de foto */

	// metodo en el controlador para ver la imagen
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id) {

		Optional<Marca> marcaActual = service.finbyId(id);

		if (marcaActual.isEmpty() || marcaActual.get().getFoto() == null) {
			return ResponseEntity.notFound().build();
		}

		// pasar la foto que es un arreglo de bytes a la respuesta por medio de un
		// ByteArrayResource importante Resource del .io
		// se crea el recurso si no esta vacio
		Resource imagen = new ByteArrayResource(marcaActual.get().getFoto());

		// se pasa la imagen al body
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}

	@PostMapping("/crear-con-foto")
	// se debe agregar un MultiparFile con la imagen
	public ResponseEntity<?> crearConFoto(@Valid Marca marca, BindingResult result, @RequestParam MultipartFile archivo)
			throws IOException {

		if (!archivo.isEmpty()) {
			// si el archivo viene vacio se lo asignamos a la marca
			marca.setFoto(archivo.getBytes());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(marca));
	}

	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editarConFoto(@Valid Marca marca, BindingResult result, @PathVariable Long id,
			@RequestParam MultipartFile archivo) throws IOException {

		Optional<Marca> marcaActual = service.finbyId(id);

		if (marcaActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Marca marcaModificadaConFoto = marcaActual.get();

		marcaModificadaConFoto.setNombre(marca.getNombre());

		if (!archivo.isEmpty()) {
			marcaModificadaConFoto.setFoto(archivo.getBytes());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(marcaModificadaConFoto));
	}

	/*
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * - - - - - - - - - - - - - - - - -
	 */
	/* Controlador para asignar "Producto a la Marca" */

	// metodo para asigar productos a la marca
	@PutMapping("/{id}/asignar-productos")
	// Se debe de obtener el Id de la tienda y pasar al body un arreglo de
	// productos(List).
	public ResponseEntity<?> asignarProducto(@RequestBody List<Producto> productos, @PathVariable Long id) {
		// primero debemos de buscar el Marca
		Optional<Marca> marcaActual = service.finbyId(id);

		if (marcaActual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// Despues se lo asignamos a otra variable
		Marca marcaModificada = marcaActual.get();

		// ahora debemos iterar la lista de productos y agregarlo a la marca por medio
		// del addProducto
		// aqui se realiza una expresion lamda donde se recibe el producto y se hace
		// algo con dicho producto, por ejemplo por cada producto se agrega
		productos.forEach(p -> {
			marcaModificada.addProducto(p);
		});

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(marcaModificada));
	}

	@PutMapping("/{id}/eliminar-producto")
	// aqui no se pasara una lista, si no que se pasa el producto
	public ResponseEntity<?> eliminarProducto(@RequestBody Producto producto, @PathVariable("id") Long Id) {

		Optional<Marca> marcaActual = service.finbyId(Id);

		if (!marcaActual.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Marca marcaModificada = marcaActual.get();

		// como es un solo alumno ya no se hace un for como en asignar

		marcaModificada.removeProducto(producto);

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(marcaModificada));
	}
}
