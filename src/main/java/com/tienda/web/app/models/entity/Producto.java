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
@Table(name = "productos")
public class Producto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nombre;
	
	//private String marca;
	
	private Integer cantidad;
	
	private Integer precio;
	
	@Column(name = "creat_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creatAt;
	
	@PrePersist
	public void prePersist() {
		this.creatAt = new Date();
	}
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	@JsonIgnore
	public byte[] foto;

	public Integer getFotoHashCode() {
		
		return (this.foto != null)? this.foto.hashCode(): null;
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

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getPrecio() {
		return precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	//vamos a sobreescribir el metodo equals
	//Este metodo es importante para eliminar un objeto de la relacion
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		//validar que los objetos sean instancia de producto
		if(!(obj instanceof Producto)) {
			return false;
		}
		
		//ahora necesitamos comparar los ID y se necesita hacer un cash del tipo producto
		Producto p = (Producto) obj;
		
		return this.id != null && this.id.equals(p.getId());
	}
	

	
}
