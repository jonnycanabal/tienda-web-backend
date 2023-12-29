package com.tienda.web.app.services;

import java.io.IOException;
import java.util.Optional;

import com.tienda.web.app.models.entity.Carrito;

public interface CarritoService {

	public Iterable<Carrito> finAll();

	public Optional<Carrito> finById(Long id);

	public Carrito save(Carrito carrito);

	public void deleteById(Long id);

	// metodo para generar factura al pagar
	byte[] pagar(Long id) throws IOException;

	// metodo para visualizar el contenido de la factura, este metodo devuelve un String
	String contenidoFactura(Long id) throws IOException;
}
