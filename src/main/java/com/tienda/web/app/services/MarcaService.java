package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.Marca;
//Aqui se realiza por decirlo asi un contrato el cual despues en una clase realizaremos la respectiva implementacion
public interface MarcaService {

	/*Iterable nos permite iterar sobre una coleccion de elementos y usar estructuras de control de bucle (for-each)*/
	public Iterable<Marca> finAll();
	
	/*Es una forma mas clara y segura de represetanr un valor null, con esto un valor podria o no existir y 
	manejarlo de forma diferentes con un get y un respectivo mensaje de la ausencia de algun dato.*/
	public Optional<Marca> finbyId(Long id);
	
	/*retorna el objeto de tipo Marca, resive la Marca antes de que se guarda y despues retorna la marca guardada*/
	public Marca save(Marca marca);
	
	/*El void ya que no retorna nada y elimina por el id*/
	public void deleteById(Long id);
}
