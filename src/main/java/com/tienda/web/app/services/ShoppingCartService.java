package com.tienda.web.app.services;

import java.io.IOException;
import java.util.Optional;

import com.tienda.web.app.models.entity.ShoppingCart;

public interface ShoppingCartService {

	public Iterable<ShoppingCart> finAll();

	public Optional<ShoppingCart> finById(Long id);

	public ShoppingCart save(ShoppingCart shoppingCart);

	public void deleteById(Long id);

	// metodo para generar factura al pagar
	byte[] generateInvoice(Long id) throws IOException;

	// metodo para visualizar el contenido de la factura, este metodo devuelve un String
	String invoiceContent(Long id) throws IOException;
}
