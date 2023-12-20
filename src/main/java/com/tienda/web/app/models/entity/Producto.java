package com.tienda.web.app.models.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "productos")
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nombre;

	private Integer cantidad;

	private Integer precio;

	@ManyToOne
	@JoinColumn(name = "marca_id")
	private Marca marca;

	// Esta anotacion no es necesaria debido a que como es una relacion ManyToMany
	// es bidireccional y ya esta establecida en Carrito
	@ManyToMany(mappedBy = "productos")
	@JsonIgnoreProperties("productos") // Esta anotacion ayuda a evitar bucles infinitos con la inforamcion al
										// convertirla a JSON
	@JsonIgnore
	private Set<Carrito> carritos = new HashSet<>();

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

		return (this.foto != null) ? this.foto.hashCode() : null;
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

	// vamos a sobreescribir el metodo equals
	// Este metodo es importante para eliminar un objeto de la relacion
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		// validar que los objetos sean instancia de producto
		if (!(obj instanceof Producto)) {
			return false;
		}

		// ahora necesitamos comparar los ID y se necesita hacer un cash del tipo
		// producto
		Producto p = (Producto) obj;

		return this.id != null && this.id.equals(p.getId());
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public Set<Carrito> getCarritos() {
		return carritos;
	}

	public void setCarritos(Set<Carrito> carritos) {
		this.carritos = carritos;
	}

}
