package com.tienda.web.app.springboot.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGreatedAuthorityJsonCreator {
	
	//contructor
	//decir en el contructor que inyecte el atributo authority
	@JsonCreator
	public SimpleGreatedAuthorityJsonCreator(@JsonProperty("authority") String role) {
		
	}

}
