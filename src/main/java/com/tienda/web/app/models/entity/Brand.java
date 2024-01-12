package com.tienda.web.app.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "brands")
@Getter
@Setter
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String brandName;

	// Relacion con la tabla productos "una marca puede tener muchos productos"
	// El FetchType.Lazy realiza una carga peresosa,cargaran los datos solamente cuando se intenta acceder a ellos.
	@OneToMany(fetch = FetchType.LAZY)
	private List<Product> products;

	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt;

	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}

	@Lob // permite persistir un large object - en este caso seria una imagen o archivos
	@Column(columnDefinition = "LONGBLOB") // modifico el atributo del campo a "LongBlob" archivos mas grandes.
	@JsonIgnore //ignora datos extensos para evitar mostarlos "Agregar dependencia en el POM"
	public byte[] photo; // esto seria un arreglo de bytes para la foto

	// Metodo para retornar un identificador de la foto al momento de usarlo con angular "FrontEnd".
	public Integer getPhotoHashCode() {
		
		// retorna un identificador unico
		return (this.photo != null) ? this.photo.hashCode() : null;
	}

	// Contructor para inicializar variables en este caso productos
	public Brand() {
		this.products = new ArrayList<>();
	}

	// metodo para agregar productos 1 a 1
	public void addProducts(Product products) {
		this.products.add(products);
	}

	// metodo para eliminar producto de la marca
	public void removeProduct(Product product) {
		this.products.remove(product);
	}

}
