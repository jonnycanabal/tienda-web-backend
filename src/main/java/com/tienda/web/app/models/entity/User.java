package com.tienda.web.app.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tienda.web.app.springbootcrud.validation.ExistsByUsername;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users") // Se le da un nombre a la tabla, de lo contrario seria el mismo de la clase
@Getter
@Setter
public class User {

	@Id // Llave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Con @GeneratedValue e Identity el ID sera "Autoincremental"
	private Long id;

	private String firtsName;
	private String middleName;
	private String lastName;
	private String seconLastName;
	private String PhoneNumber;
	private String email;

	@ExistsByUsername //anotacion de validacion personalizada
	@Column(unique = true)
	@NotBlank
	@Size(min = 4, max = 12)
	private String username;

	@NotBlank
	@JsonProperty(access = Access.WRITE_ONLY) //Solo muestra cuando se escribe o crea
	private String password;

	@JsonIgnoreProperties({"users"}) //de la lista de roles se ignora los usuarios, esto evita un bucle
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"),
			uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})})
	private List<Role> roles;
	
	//la lista de roles se asigna a un contructor vacio
	public User() {
		roles = new ArrayList<>();
	}
	
	@Column(name = "enabled")
	private boolean enabled;
	
//	@PrePersist
//	public void prePresist() {
//		enabled = true;
//	}
	
	//es una bandera y no esta mapeado a la tabla
	@Transient
	//@JsonProperty(access = Access.WRITE_ONLY)
	private boolean admin;

	// mappedBy indica la relacino mapeada por usuario en la entidad carrito
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("user") // evita bucles infinitos al convertir la informacion a JSON
	private ShoppingCart shoppingCart;

	@Column(name = "create_at") // Con Column le damos un nombre diferente en nuestra tabla
	@Temporal(TemporalType.TIMESTAMP) // Con esta anotacion nuestra fecha sera completa con fecha y hora
	private Date createAt;

	// Metodo para que de forma automatica se haga un insert en la base de datos y
	// asigne la fecha
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id) && Objects.equals(username, other.username);
	}

}
