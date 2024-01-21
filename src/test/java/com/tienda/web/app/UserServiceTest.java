package com.tienda.web.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tienda.web.app.models.entity.Role;
import com.tienda.web.app.models.entity.User;
import com.tienda.web.app.models.repository.RoleRepository;
import com.tienda.web.app.models.repository.UserRepository;
import com.tienda.web.app.services.UserServiceImplement;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
//@DataJpaTest
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RoleRepository rolRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserServiceImplement service;
	
	private User user;
    private Role userRole;
	
	@BeforeEach
	public void testUser() {
		
		user = new User();
		
		user.setId((long) 1);
		user.setFirtsName("Diego");
		user.setMiddleName("");
		user.setLastName("Briñez");
		user.setSeconLastName("");
		user.setPhoneNumber("3112587495");
		user.setEmail("pumba@gmail.com");
		user.setUsername("pumba");
		user.setPassword("12345");
		user.setAdmin(false);
		
		userRole = new Role();
		userRole.setName("ROLE_USER");
		
	}
	
	@Test
	public void testFindAll() {
		
		// 1. Se crea una lista con los usuarios "User"
		List<User> userList = Arrays.asList(user);
		
		// 2. Uso de mockito para crear un objeto simulado del repository, y retorna en este caso la lista creada
		// Asi se simula el comportamiento del repository sin tocar los datos reales de la DB
		Mockito.when(userRepository.findAll()).thenReturn(userList);
		
		// Se llama el metodo en este caso el finAll() del service, pero llamara el metodo mock creado del repository
		Iterable<User> result = service.finAll();
		
		// Se verifica que la lista resultante del result sea igual a la lista creada al princio
		assertEquals(userList, result);
	}
	
	@Test
	public void testFindById() {
		// se configura el Mockito para para que reciba cualquier long del objeto user
		Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
		
		// con el optional llamamos el metodo findbyid del servicio
		Optional<User> response = service.findById(1L);
		
		// Verifica si el optional devuelto del service contiene un valor
		assertTrue(response.isPresent());
		
		// Verifica que le id del usuario en el optional sea igual al id del user
		assertEquals(user.getId(), response.get().getId());
	}
	
	@Test
	public void testSave() {
		
		// Configuracion del mock Rolrepository, esto simula la busqueda de un rol por nombre en la BD
		Mockito.when(rolRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
		
		// Simulacion del proceso de codigicacion de la contraseña
		Mockito.when(passwordEncoder.encode("12345")).thenReturn("encodedPassword");
		
		// comportamiento del save del Repository, simulando el guardado en la BD
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
		// llamar al metodo save del service con el objeto "user"
		User result = service.save(user);
		
		// Verificar que no sea nulo
		assertNotNull(result);
		
		// Verificar que la contraseña sea la esperada
		assertEquals("encodedPassword", result.getPassword());
		
		// verificar que el resultado no sea administrador, es decir retorna un false
		assertFalse(result.isAdmin());
	}
	
	@Test
	public void testDeleteUser() {
		
		//Mockito.when no se puede usar en delete debido a que no tiene valor de retorno
		
		// Crear un Id de usuario para probar
		Long userId = (long) 1;
		
		// Se llama al metodo deleteById del servicis con el id de prueba
		service.deleteById(userId);
		
		// Mockito.times indica cuantas veces se va a llamar el metodo en este caso 1 vez
		// Verificas que el metodo delete del repository sea llamado 1 vez con el id de user
		Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
	}

	@Test
	public void testFindUser() {
		
		// Configurar el comportamiento del mock del Repository a probar, 
		// este retorna una lista que contiene un objeto user
		Mockito.when(userRepository.findByFirtsNameContainingIgnoreCaseOrMiddleNameIgnoreCaseOrLastNameIgnoreCaseOrSeconLastNameIgnoreCase(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(List.of(user));
		
		//Se llama al metodo del service con unos valores especificios para probar
		List<User> response = service.findByFirtsNameContainingIgnoreCaseOrMiddleNameIgnoreCaseOrLastNameIgnoreCaseOrSeconLastNameIgnoreCase(
				"Diego", "", "Briñez", "");
		
		// Verificar que el response o la lista anterior no sea nula
		assertNotNull(response);
		
		// Verificas que la respuseta contenga exactamente 1 elemento
		assertEquals(1, response.size());
		
		// Se obtiene el usuario recuperado del response
		User retrivedUser = response.get(0);
		
		// Verificas que el nombre del user, sea igual al nombre recuperado del response
		assertEquals(user.getFirtsName(), retrivedUser.getFirtsName());
	}
}
