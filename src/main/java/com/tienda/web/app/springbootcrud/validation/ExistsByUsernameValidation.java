package com.tienda.web.app.springbootcrud.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tienda.web.app.services.UserService;

@Component//Permite inyectar, en este caso el repositorio, service, etc y asi validar
//La clase implementa la interfaz ConstraintValidator con las anotacion ExistsByUsername de tipo String
public class  ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

	@Autowired
	private UserService service;
	
	@Override
	//llama al metodo durante la validacion y veririca si el username existe - si existe retorna false
	public boolean isValid(String username, ConstraintValidatorContext context) {
		
		if(service == null) {
			return true;
		}

		return !service.existsByUsername(username);
	}
}
