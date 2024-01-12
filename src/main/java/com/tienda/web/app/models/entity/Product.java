package com.tienda.web.app.models.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

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
@Table(name = "products")
@Getter
@Setter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productName;

	private Integer price;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;

	@ManyToMany(mappedBy = "products")
	@JsonIgnoreProperties("products") // Esta anotacion ayuda a evitar bucles infinitos con la inforamcion al convertirla a JSON
	@JsonIgnore
	private Set<ShoppingCart> shoppingCarts = new HashSet<>();

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
	public byte[] photo;

	public Integer getPhotoHashCode() {

		return (this.photo != null) ? this.photo.hashCode() : null;
	}

	// vamos a sobreescribir el metodo equals
	// Este metodo es importante para eliminar un objeto de la relacion
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		// validar que los objetos sean instancia de producto
		if (!(obj instanceof Product)) {
			return false;
		}

		// ahora necesitamos comparar los ID y se necesita hacer un cash del tipo
		// producto
		Product p = (Product) obj;

		return this.id != null && this.id.equals(p.getId());
	}

}
