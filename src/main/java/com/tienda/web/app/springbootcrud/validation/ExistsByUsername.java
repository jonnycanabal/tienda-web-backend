package com.tienda.web.app.springbootcrud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ExistsByUsernameValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByUsername {
	
	//Se da control click en la anotacion NotBlank de User y se copia estas lineas de codigo
	String message() default " Ya existe en la base de datos!, escoja otro username";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
