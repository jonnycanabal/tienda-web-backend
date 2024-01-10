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
public class JwtValidationFilter extends BasicAuthenticationFilter{

	//Constructor
	public JwtValidationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	//Metodo
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		//primero se obtiene el token de la cabecera (header)
		String header = request.getHeader(HEADER_AUTHORIZATION);
		
		//se valida que distinto a null
		if(header == null || !header.startsWith(PREFIX_TOKEN)) {
			chain.doFilter(request, response);
			return;
		}
		
		//Aqui solo obtenemos el token sin el prefijo bearer
		String token = header.replace(PREFIX_TOKEN, "");
		
		try {
			//Se valida el token, con un parse, se verifica la llave, si no es valido manda al catch
			Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
			
			//obtener el username
			String username = claims.getSubject();
			//String username2 = (String) claims.get("username");
			
			//obtener los roles en este caso se obtiene como un tipo object con estructura string
			Object authoritiesClaims = claims.get("authorities");
			
			//el Object se pasa y se convierte a un tipo real GrantedAuthority, se convierte a un arreglo de bytes
			//A un tipo collection ya que es un arreglo de roles	
			Collection<? extends GrantedAuthority> authorities = Arrays.asList(
					new ObjectMapper()
					.addMixIn(SimpleGrantedAuthority.class, 
								SimpleGreatedAuthorityJsonCreator.class)
					.readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
					);
			
			//Se inicia sesion con UsernamePasswordAuthenticationToken, se crea el token de authentication
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
			
			//Se autentica con SecurityContextHolder
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
			//cadena de filtros si todo sale bien continua
			chain.doFilter(request, response);
			
		} catch (JwtException e) {
			Map<String, String> body = new HashMap<>();
			body.put("error", e.getMessage());
			body.put("message", "El token JWT es invalido!");
			
			response.getWriter().write(new ObjectMapper().writeValueAsString(body));
			response.setStatus(HttpStatus.UNAUTHORIZED.value());//no esta autorizado 401
			response.setContentType(CONTENT_TYPE);
		}

	}
	
}
