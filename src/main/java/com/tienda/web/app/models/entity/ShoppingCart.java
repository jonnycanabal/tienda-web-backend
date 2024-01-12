package com.tienda.web.app.models.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shoppingCarts")
@Getter
@Setter
public class ShoppingCart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creatAt;

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties("shoppingCart") // Esta anotacion ayuda a evitar bucles infinitos con la inforamcion al convertirla a JSON
	private User user;

	@ManyToMany
	@JoinTable(name = "shoppingCart_product", joinColumns = @JoinColumn(name = "shoppingCart_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private Set<Product> products = new HashSet<>();

	@OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Set<CartItem> items = new HashSet<>();

	@PrePersist
	public void prePersist() {
		this.creatAt = new Date();
	}

	// Metodo para calcular el total de los productos del carrito
	public double calculateTotal() {
		return items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
	}

}
