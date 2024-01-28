package com.tienda.web.app.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.ShoppingCart;
import com.tienda.web.app.models.entity.CartItem;
import com.tienda.web.app.models.entity.User;
import com.tienda.web.app.models.repository.ShoppingCartRepository;

@Service
public class ShoppingCartServiceImplement implements ShoppingCartService {

	@Autowired
	private ShoppingCartRepository repository;

	@Override
	public Iterable<ShoppingCart> finAll() {
		return repository.findAll();
	}

	@Override
	public Optional<ShoppingCart> finById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public ShoppingCart save(ShoppingCart shoppingCart) {
		return repository.save(shoppingCart);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	public byte[] generateInvoice(Long id) throws IOException {

		// Buscar el carrito por su ID en la base de datos
		Optional<ShoppingCart> currentShoppingCart = repository.findById(id);

		// Verificamos que el carrito exista
		if (!currentShoppingCart.isPresent()) {
			return new byte[0];
		}

		// Optenemos el carrito y el usuario asociado
		ShoppingCart shoppingCart = currentShoppingCart.get();
		User user = shoppingCart.getUser();

		// creo el documento
		PDDocument document = new PDDocument();

		// creo la pagina con sus tamaño y agrego la pagina a mi documento
		PDPage page = new PDPage(PDRectangle.A4);
		document.addPage(page);

		// crear el flujo de contenido para escribir en la pagina
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// configurar el formato, informacion y contenido de la pagina
		contentStream.beginText();
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
		contentStream.newLineAtOffset(50, 750);
		contentStream.showText("Factura de Compra");
		contentStream.newLineAtOffset(0, -20);
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream.showText("Fecha: " + LocalDate.now());
		contentStream.newLineAtOffset(0, -20);
		contentStream.showText("Cliente: " + user.getFirtsName() + " " + user.getLastName());
		contentStream.newLineAtOffset(0, -20);
		contentStream.showText("Productos: ");

		// iteramos en los productos del carrito para asi agrearlos al contenido de la
		// pagina
		for (CartItem item : shoppingCart.getItems()) {
			contentStream.newLineAtOffset(0, -15);
			contentStream.showText("- " + item.getProduct().getProductName() + ", Precio: "
					+ item.getProduct().getPrice() + ", cantidad: " + item.getQuantity());

		}

		contentStream.newLineAtOffset(0, -20);
		contentStream.showText("Total: $" + shoppingCart.calculateTotal());

		contentStream.endText();

		contentStream.close();

		// Convertir el documento a PDF a bytes y cerramos el documento
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		document.save(byteArrayOutputStream);
		document.close();

		// regresamos el contenido del PDF en un arreglo de bytes
		return byteArrayOutputStream.toByteArray();
	}

	// implementacion del metodo para visualizar el contenido de la factura con
	// PDFBOX
	@Override
	public String invoiceContent(Long id) throws IOException {

		// Llamar al metodo existente para obtener el contenido de la factura en formado
		// PDF como un arrelgo de bytes
		byte[] contentPDF = generateInvoice(id);

		// Cargamos el contenido del PDF desde el arreglo de bytes (PDDocuent - clase de
		// PDFBox)
		PDDocument document = PDDocument.load(new ByteArrayInputStream(contentPDF));

		// Se crea un objeto StringWriter que actua como un contenedor para el texto
		// extraido del PDF
		StringWriter writer = new StringWriter();

		// Crea un objeto PDFTextStripper que es una clase de PDFBox que se utiliza para
		// extraer texto de un documento PDF
		PDFTextStripper stripper = new PDFTextStripper();

		// se utiliza el stripper para extraer el texto del documento PDF y escribirlo
		// en el StringWriter
		stripper.writeText(document, writer);
		document.close();

		return writer.toString();
	}

}
