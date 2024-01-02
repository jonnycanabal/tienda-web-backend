package com.tienda.web.app.models.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users") // Se le da un nombre a la tabla, de lo contrario seria el mismo de la clase
public class User {

	@Id //Llave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Con @GeneratedValue e Identity el ID sera "Autoincremental"
	private Long id;

	private String firtsName;
	private String middleName;
	private String lastName;
	private String seconLastName;
	private Long PhoneNumber;
	private String email;

	// mappedBy indica la relacino mapeada por usuario en la entidad carrito
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("user") // evita bucles infinitos al convertir la informacion a JSON
	private ShoppingCart shoppingCart;

	@Column(name = "create_at") // Con Column le damos un nombre diferente en nuestra tabla
	@Temporal(TemporalType.TIMESTAMP) // Con esta anotacion nuestra fecha sera completa con fecha y hora
	private Date createAt;

	// Metodo para que de forma automatica se haga un insert en la base de datos y asigne la fecha
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirtsName() {
		return firtsName;
	}

	public void setFirtsName(String firtsName) {
		this.firtsName = firtsName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSeconLastName() {
		return seconLastName;
	}

	public void setSeconLastName(String seconLastName) {
		this.seconLastName = seconLastName;
	}

	public Long getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(Long PhoneNumber) {
		this.PhoneNumber = PhoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

}
