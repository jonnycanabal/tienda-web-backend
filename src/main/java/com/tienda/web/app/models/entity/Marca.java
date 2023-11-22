package com.tienda.web.app.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name="marcas") // le estoy dando un nombre diferente a la tabla, si no se hace el nombre seria igual al de la clase
public class Marca {
	
	@Id //Se marca como nuestra llave principal de nuestra base de datos
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Con GeneratedValue e Identity hacemos que nuestro id sea autoincremental
	private Long id;
	
	private String nombre;
	
	//Relacion con la tabla productos "una marca puede tener muchos productos"
	// con el fetch realiza la consulta
	//Se usa Lazy que es una carga peresosa,se realiza la consulta pero con esto se cargaron los datos solamente cuando se intente acceder a ellos.
	@OneToMany(fetch = FetchType.LAZY)
	private List<Producto> productos;
	
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
	//*************** QUEDE AQUI ***********
	//Contructor para inicializar variables en este caso productos
	public Marca() {
		this.productos = new ArrayList<>();
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

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	
	//metodo para agregar productos 1 a 1
	//tambien se debe crear la instancia de prouductos ya que no esta definido, este se debe hacer en el contructor para inicializar.
	public void addProducto(Producto producto) {
		this.productos.add(producto);
	}
	
	//metodo para eliminar producto de la marca
	public void removeProducto(Producto producto) {
		this.productos.remove(producto);
	}
}
