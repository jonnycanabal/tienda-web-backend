package com.tienda.web.app.models.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="marcas") // le estoy dando un nombre diferente a la tabla, si no se hace el nombre seria igual al de la clase
public class Marca {
	
	@Id //Se marca como nuestra llave principal de nuestra base de datos
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Con GeneratedValue e Identity hacemos que nuestro id sea autoincremental
	private Long id;
	
	private String nombre;
	
	//private String producto;
	
	@Column(name = "create_at")//Con Column le damos un nombre diferente en nuestra tabla de la base de datos
	@Temporal(TemporalType.TIMESTAMP)  //con esta anotacion hacemos que nuestra fecha sea completa con fecha y hora
	private Date createAt;

	//metodo para que de forma automatica se haga un insert para que en la base de datos se asigne la fecha
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}
	
	@Lob //permite persistir un large object - en este caso seria una imagen o almacenar archivos
	@Column(columnDefinition = "LONGBLOB")//modifico el atributo del campo "LongBlob" para recibir archivos mas grandes.
	/*el constenido que se almacena es binario lo cual es muy extenso y no tiene importancia 
	y no es necesario mostrarlo en el contenido, por ente se tendria que omitir. esto se realiza con el JsonIgnore, 
	recordar agregar la dependencia en el Pom*/
	@JsonIgnore
	public byte[] foto;//esto seria un arreglo de bytes para la foto
	
	//este metodo es para retornar un identificador de la foto al momento de usarlo con angular.
	public Integer getFotoHashCode() {
		//retorna un identificador unico
		return (this.foto != null) ? this.foto.hashCode(): null;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	
	
}
