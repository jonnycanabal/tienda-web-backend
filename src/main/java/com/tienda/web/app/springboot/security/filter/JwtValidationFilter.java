package com.tienda.web.app.springboot.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.web.app.springboot.security.SimpleGreatedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import static com.tienda.web.app.springboot.security.TokenJwtConfig.*;

//para la validacion del filtro se hereda de una clase diferente BasicAuthenticationFilter
//Se encarga de la validacion del Token durante el proceso de filtrado en la cadena de filtros de Spring Security.
public class JwtValidationFilter extends BasicAuthenticationFilter{

	//Constructor
	public JwtValidationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	//Metodo para realizar la validacion del Token JWT, logica de validacion del "token".
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		//primero se obtiene el token de la cabecera (header)
		String header = request.getHeader(HEADER_AUTHORIZATION);
		
		//se valida que el token tenga el prefijo "Bearer " y que no sea null
		if(header == null || !header.startsWith(PREFIX_TOKEN)) {
			chain.doFilter(request, response);
			return;
		}
		
		//Aqui solo obtenemos el token sin el prefijo bearer
		String token = header.replace(PREFIX_TOKEN, "");
		
		try {
			//Se valida el token, con un parse, se verifica la llave, si no es valido manda al catch
			//Esta en la documentacion https://github.com/jwtk/jjwt
			Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
			
			//obtener el username
			String username = claims.getSubject();
			//String username2 = (String) claims.get("username");
			
			//obtener los roles en este caso se obtiene como un tipo object con estructura string
			Object authoritiesClaims = claims.get("authorities");
			
			//el Object se pasa y se convierte a un tipo real GrantedAuthority, se convierte a un arreglo de bytes
			//A un tipo collection ya que es un arreglo de roles	
			Collection<? extends GrantedAuthority> authorities = Arrays.asList(
					//Se crea instancia de la clase ObjectMapper para mapear objetos java, Json y viceversa
					new ObjectMapper()
					//Mix-in Asocia una clase segundaria con una clase logica definida en este caso en
							//SimpleGreatedAuthorityJsonCreator para SimpleGrantedAuthority
					.addMixIn(SimpleGrantedAuthority.class,
								SimpleGreatedAuthorityJsonCreator.class)
					//Se utiliza para leer el valor de authoritiesClaims (que es un objeto)
					//y convertirlo a una matriz de objetos SimpleGrantedAuthority
					.readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
					);
			
			//Se crea objeto UsernamePasswordAuthenticationToken, representa informacion de autenticacion
			//Se encapsulan nombre de usuario, (credenciales null - autenticacion basa en token no necesita
			// // y authorities (roles))
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
			
			//Se establece objeto SecurityContextHolder que indica el usuario se autentico y tiene derecho a acceder
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
			//cadena de filtros si todo sale bien continua
			chain.doFilter(request, response);

		// Bloque catch de excepciones que pueden ocurrir durante la validacion del token JWT
		} catch (JwtException e) {
			//Se crea un map con HasMap para contruir un cuerpo con Key y argumento
			Map<String, String> body = new HashMap<>();
			//Se agrega mensaje de la excepcion del error y un mensaje adicion de invalides
			body.put("error", e.getMessage());
			body.put("message", "El token JWT es invalido!");

			//Se usa ObjectMapper para convertir en mapa 'Body' en formato JSON y
			//se escribe en el cuerpo de la respuesta.
			response.getWriter().write(new ObjectMapper().writeValueAsString(body));
			response.setStatus(HttpStatus.UNAUTHORIZED.value());//Se establece el status - no esta autorizado 401
			response.setContentType(CONTENT_TYPE); //Se establece el tipo de contenido de la respuesta JSON
		}

	}
	
}
