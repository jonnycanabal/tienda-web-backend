package com.tienda.web.app.springbootcrud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ExistsByUsernameValidation.class) //Logica real se implementa en la clase señalada
@Target(ElementType.FIELD) //Solo se aplica a campos
@Retention(RetentionPolicy.RUNTIME) //Estara en tiempo de ejecucion
public @interface ExistsByUsername {
	
	//Se da control click en la anotacion NotBlank de User y se copia estas lineas de codigo
	String message() default " Ya existe en la base de datos!, escoja otro username"; //Mensaje Predeterminado

	Class<?>[] groups() default { }; //Define grupos de validacion

	Class<? extends Payload>[] payload() default { }; //Informacion adicional

}
