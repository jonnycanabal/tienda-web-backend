package com.tienda.web.app.springbootcrud.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tienda.web.app.services.UserService;

@Component//para poder inyectar el repositorio, service, etc y asi validar
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

	@Autowired
	private UserService service;
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		
		if(service == null) {
			return true;
		}

		return !service.existsByUsername(username);
	}

}
