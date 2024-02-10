package com.tienda.web.app.springboot.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGreatedAuthorityJsonCreator {
	
	//contructor
	//decir en el contructor que inyecte el atributo authority
	@JsonCreator//indica a Jackson utilizar contructor para la deserializar objetos de esta clase
	//Clase de Spring Security para representar una autoridad o un rol en el sistema
	//authority se utiliza para almacenar el nombre del rol
	public SimpleGreatedAuthorityJsonCreator(@JsonProperty("authority") String role) {
		
	}

}
