package com.tienda.web.app.springboot.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DataBindingException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.exc.StreamReadException;
//import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.web.app.models.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

//se realizar los import de las constantes de la clase creada TokenJwtConfig / como son estaticas, es con static
import static com.tienda.web.app.springboot.security.TokenJwtConfig.*;

//Se hereda de una clase de Spring
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	// Se debe implementar el contructor para pasar un atributo

	// atributo utilizado para autenticar usuarios
	private AuthenticationManager authenticationManager;

	// se pasa por medio del contructor / este se encarga de autenticar los usuarios
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {

		this.authenticationManager = authenticationManager;
	}

	//Metodo que se ejecuta cuando se intenta autenticar un usuario, extrae la informacion del usuario
	//
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		// Inicializacion de variables para su posterior uso
		User user = null;
		String username = null;
		String password = null;

		try {
			// ObjectMapper convierte el cuerpo de la solicitud en un objeto "User"
			// Se Extae el username y passoword el objeto "User"
			// Stream se usa para manejar entrada de datos desde solicitudes HTTP
			user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			username = user.getUsername();
			password = user.getPassword();
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DataBindingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// se crea el UsernamePasswordAuthenticationToken con el nombre de usuario y
		// contraseña extraidos
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		return authenticationManager.authenticate(authenticationToken);

	}

	//Se va a sobreescribir el metodo successfulAuthentication
	//Se ejecuta si la autenticacion es exitosa, obtiene el usuario y roles del objeto authentication
	//Devuelte tambien un JSON con informacion adicional como el token y un mensaje de saludo
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		// Se obtiene el User de spring security - representa el usuario autenticado
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult
				.getPrincipal();

		// Aqui se extrae el username
		String username = user.getUsername();

		//Se optiones los roles del usuario (Authorities)
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

		// Claims son datos
		//Se crean los claims que se incluiran en el token JWT en este caso los roles y username
//		Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(roles))
//				.add("username", username).build();
		
		Claims claims = Jwts.claims()
				.add("authorities", new ObjectMapper().writeValueAsString(roles))
				.add("username", username)
				.build();

		// Aqui generamos el "TOKEN"
		 // usuario - el subject es un claim por defecto y es obligatorio para el username
		//Se contruye el token con informacion previamente configurada, usuario, claims y otros detalles
		//como fecha, tiempo de expiracion y la firma.
		String token = Jwts.builder()
				.subject(username)
				.claims(claims)
				.expiration(new Date(System.currentTimeMillis() + 3600000))
				.issuedAt(new Date())
				.signWith(SECRET_KEY) 
				.compact();

		// Devolvemos el token al cliente (A la vista)
		// Bearer es un estandar del tipo de dato JWT
		response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

		//Se crea un mapa que contiene informacion adicional para ser devuelta en formato JSON
		Map<String, String> body = new HashMap<>();
		body.put("token", token);
		body.put("username", username);
		body.put("message", String.format("Hola %s has iniciado sesion con exito!", username));

		// Escribir el JSON en la respuesta, se convierte el map en un en un JSON con
		// ObjectMapper y writeValueAsString
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(200);
	}

	// se va a sobreescribir el metodo unsuccessfulAuthentication - si no es valida la autenticacion
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		//Se crea un Mapa (boby) para almacenar la informacion
		Map<String, String> body = new HashMap<>();

		//Se agrega al Map un mensaje de error indicando que hubo un problema y el error especifico
		// buena practica, no decir en que esta mal por temas de seguridad
		body.put("message", "Error en la autenticacion usuario o contraseña incorrectos!");
		body.put("error", failed.getMessage());

		//Se escribe el cuerpo de la respuesta JSON
		//Se utiliza el objeto ObjectMapper para convertir el map(body) en una cadena JSON
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401); // no esta autorizados
		response.setContentType(CONTENT_TYPE);//Se establece el tipo de contenido en la respuesta application/json
	}

}