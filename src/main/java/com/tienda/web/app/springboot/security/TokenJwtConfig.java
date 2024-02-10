package com.tienda.web.app.springboot.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {
	
	//Llave secreta, final = no se puede modificar, Key = de java.security
	//Esta llave no viajara a ninguna parte y se quedara solo en el servidor
	
	//recordar que como estan ahora en una clase deben ser "public"

	//Linea para definiar la llave secreata que firmara los Token
	//Documentacion https://github.com/jwtk/jjwt para asignar la secretKey
	public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

	//Prefijo utilizado en el encabeza de autorizacion JWT - Por convencio mucho citios Web
	//usan el prefixs Bearer seguido de un espacio
	public static final String PREFIX_TOKEN = "Bearer ";

	//Nombre del encabezado de autorizacion utilizado al enviar el Token JWT en solicitudes HTTP
	public static final String HEADER_AUTHORIZATION = "Authorization";

	//Define tipo de contenido utilizado en la respusta HTTP relacionadas con el JWT
	public static final String CONTENT_TYPE = "application/json";


//	esta clase TokenJwtConfig proporciona configuraciones y constantes relacionadas con la generación y
//	manipulación de tokens JWT, incluida la llave secreta, el prefijo del token,
//	el nombre del encabezado de autorización y el tipo de contenido.

}
