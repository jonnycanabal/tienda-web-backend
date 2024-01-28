package com.tienda.web.app.controllers;

import java.util.Map;
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

import com.tienda.web.app.models.entity.Role;
import com.tienda.web.app.services.RoleService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService service;
	
	@GetMapping
	public ResponseEntity<?> list(){
		
		return ResponseEntity.ok().body(service.finAll());
	}
	
	@PostMapping("/ver/{id}")
	public ResponseEntity<?> view(@PathVariable Long id){
		
		Optional<Role> currentRole = service.findById(id);
		
		if(!currentRole.isPresent()) {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Rol no existe");
		}
		
		return ResponseEntity.ok().body(currentRole.get());
	}
	
	@PostMapping("/buscar")
	public ResponseEntity<?> findRole(@RequestBody Map<String, String> requestParams){
		
		String roleName = requestParams.get("roleName");
		
		Optional<Role> currentRole = service.findByName(roleName);
		
		if(!currentRole.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El rol no existe");
		}
		
		return ResponseEntity.ok().body(currentRole);
	}
	
	@PostMapping("/crear")
	public ResponseEntity<?> create(@RequestBody Role role){
		
		Role newRole = service.save(role);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
	}
	
	@PutMapping("/editar/{id}")
	public ResponseEntity<?> edit(@PathVariable Long id, @RequestBody Role role){
		
		Optional<Role> currentRole = service.findById(id);
		
		if (!currentRole.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Role roleToModify = currentRole.get();
		
		if(role.getName() != null) {
			roleToModify.setName(role.getName());
		}
		
		Role updatedRole = service.save(roleToModify);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedRole);
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> delate(@PathVariable Long id){
		
		service.delateById(id);
		
		return ResponseEntity.noContent().build();
	}

}
