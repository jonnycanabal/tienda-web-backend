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
@Table(name = "products")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
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

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Set<ShoppingCart> getShoppingCarts() {
		return shoppingCarts;
	}

	public void setShoppingCarts(Set<ShoppingCart> shoppingCarts) {
		this.shoppingCarts = shoppingCarts;
	}

}
