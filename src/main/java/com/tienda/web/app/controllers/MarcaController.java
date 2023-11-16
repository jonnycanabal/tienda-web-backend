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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tienda.web.app.models.entity.Marca;
import com.tienda.web.app.services.MarcaService;

import jakarta.validation.Valid;

/*@RestController, devuelve los datos en formato Json y sirve para manejar respuesta HTTP y 
tambien hacer las anotacion con los get, post, put, delet a una url determinada.*/
@RestController
public class MarcaController {

	//Recordar importar la interfaz ya que es un tipo generico el cual se crea. ya que va ser la misma y comun.
	@Autowired
	private MarcaService service;
	
	// dar una ruta Url a la pedicion que se va a dar en este caso Get.
	@GetMapping("/tienda")
	public ResponseEntity<?> listar(){
		
		//se pasa al cuerpo (body)de la respuseta una linda de entity.
		return ResponseEntity.ok().body(service.finAll());
	}
	
	@GetMapping("/tienda/ver/{id}")
	//El PathVariable se usa para extraer valores de variables a nuestra URL y mapearlos al parametro del metodo.
	public ResponseEntity<?> ver(@PathVariable Long id){
		
		//tenemos que buscar nuestro objeto y validaremos que exista en nuestra base de datos
		Optional<Marca> o = service.finbyId(id);
		
		if(o.isEmpty()) {
			//el build contruye la respuesta sin contenido
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(o.get());
	}
	
	@PostMapping("/tienda/crear")
	//se utiliza el RequestBody ya que necesitamos agregar la Marca la cual viene del Request, del cuerpo (body)
	public ResponseEntity<?> crear(@RequestBody Marca marca){
		//Se almacena el alumno creado en una variable la cual se pasara al cuerpo de la respuesta.
		Marca marcaDb = service.save(marca);
		return ResponseEntity.status(HttpStatus.CREATED).body(marcaDb);
	}
	
	@PutMapping("/tienda/editar/{id}")
	//se agregar la marca y el id para asi referencia lo que se va a editar.
	public ResponseEntity<?> editar(@RequestBody Marca marca, @PathVariable Long id){
		//primero buscamos la marca en la Base de Datos
		Optional<Marca> o = service.finbyId(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//aqui optenemos nuestra marca
		Marca marcaDb = o.get();
		
		//luego con nuestro metodo set() cambiamos el valor y el metodo get para obtener el valor del Request. 
		marcaDb.setNombre(marca.getNombre());
		
		//primero debemos persistir o guardalo antes de pasarlo al boty con el service.save
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(marcaDb));
	}
	
	@DeleteMapping("/tienda/eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		service.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
												/* Controlador de foto*/
	
	//metodo en el controlador para ver la imagen
	@GetMapping("/tienda/uploads/img/{id}")
	public ResponseEntity<?>verFoto(@PathVariable Long id){
		Optional<Marca> o = service.finbyId(id);
		
		if(o.isEmpty() || o.get().getFoto() == null) {
			return ResponseEntity.notFound().build();
		}
		
		//pasar la foto que es un arreglo de bytes a la respuesta por medio de un ByteArrayResource importante Resource del .io
		//se crea el recurso si no esta vacio
		Resource imagen = new ByteArrayResource(o.get().getFoto());
		
		//se pasa la imagen al body
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}
	
	@PostMapping("/tienda/crear-con-foto")
	//se debe agregar un MultiparFile con la imagen
	public ResponseEntity<?> crearConFoto(@Valid Marca marca, BindingResult result, @RequestParam MultipartFile archivo) 
			throws IOException{
		
		if(!archivo.isEmpty()) {
			//si el archivo viene vacio se lo asignamos a la marca
			marca.setFoto(archivo.getBytes());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(marca));
	}
	
	@PutMapping("/tienda/editar-con-foto/{id}")
	public ResponseEntity<?> editarConFoto(@Valid Marca marca, BindingResult result, @PathVariable Long id, 
			@RequestParam MultipartFile archivo) throws IOException{

		Optional<Marca> o = service.finbyId(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Marca marcaDb = o.get();
		
		marcaDb.setNombre(marca.getNombre());
		
		if(!archivo.isEmpty()) {
			marcaDb.setFoto(archivo.getBytes());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(marcaDb));
	}
	
}
