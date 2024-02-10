package com.tienda.web.app.springboot.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

//AccessDeniedHandler interfaz de Spring Security, utilizada para manejar las excepciones de acceso denegado
//La implementacion personalizada de CustomAccessDeniedHandler se encargara de manejar la situacion acceso denegado
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	//handle este metodo es llamda cuando se produce una excepcion de acceso denegado
	//HttpServletRequest request: Representa la solicitud HTTP.
	//  Contiene información sobre la solicitud del cliente, como la URL solicitada, parámetros, encabezados, etc.
	//HttpServletResponse response: Representa la respuesta HTTP que se enviará al cliente.
	//  Permite personalizar la respuesta, como establecer códigos de estado, encabezados de respuesta, contenido, etc.
	//AccessDeniedException accessDeniedException: Es una excepción lanzada por Spring Security
	//   cuando se deniega el acceso a un usuario. Contiene información sobre la razón de la denegación de acceso,
	//   como los roles necesarios que faltan, entre otras cosas.
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		//Respuesta SC_Forbidden (403) acceso denegado, da mensaje detallada del accessDeniedException
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: " +
				accessDeniedException.getMessage());
	}

}
