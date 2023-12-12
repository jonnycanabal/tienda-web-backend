package com.tienda.web.app.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.Carrito;
import com.tienda.web.app.models.entity.ItemCarrito;
import com.tienda.web.app.models.entity.Usuario;
import com.tienda.web.app.models.repository.CarritoRepository;

@Service
public class CarritoServiceImplement implements CarritoService {

	@Autowired
	private CarritoRepository repository;
	
	@Override
	public Iterable<Carrito> finAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Carrito> finById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Carrito save(Carrito carrito) {	
		return repository.save(carrito);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	
	public byte[] pagar(Long id) throws IOException {
		
		//Buscar el carrito por su ID en la base de datos
		Optional<Carrito> o = repository.findById(id);
		
		//Verificamos que el carrito exista
		if(o.isEmpty()) {
			return new byte[0];
		}
		
		//Optenemos el carrito y el usuario asociado
		Carrito carrito = o.get();
		Usuario usuario = carrito.getUsuario();
		
		//creo el documento
		PDDocument document = new PDDocument();
		
		//creo la pagina con sus tamaño y agrego la pagina a mi documento
		PDPage pagina = new PDPage(PDRectangle.A4);
		document.addPage(pagina);
		
		//crear el flujo de contenido para escribir en la pagina
		PDPageContentStream contenidoStream = new PDPageContentStream(document, pagina);
		
		//configurar el formato, informacion y contenido de la pagina
		contenidoStream.beginText();
		contenidoStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
		contenidoStream.newLineAtOffset(50, 750);
		contenidoStream.showText("Factura de Compra");
		contenidoStream.newLineAtOffset(0, -20);
		contenidoStream.setFont(PDType1Font.HELVETICA, 12);
		contenidoStream.showText("Fecha: " + LocalDate.now());
		contenidoStream.newLineAtOffset(0, -20);
		contenidoStream.showText("Cliente: " + usuario.getPrimerNombre() + " " + usuario.getPrimerApellido());
		contenidoStream.newLineAtOffset(0, -20);
		contenidoStream.showText("Productos: "); 
		
		//iteramos en los productos del carrito para asi agrearlos al contenido de la pagina
		for (ItemCarrito item : carrito.getItems()) {
			contenidoStream.newLineAtOffset(0, -15);
			contenidoStream.showText("- " + item.getProducto().getNombre() +
					", Precio: " + item.getProducto().getPrecio());
			
		}
		
		contenidoStream.newLineAtOffset(0, -20);
		contenidoStream.showText("Total: $" + carrito.total());
		
		contenidoStream.endText();
		
		contenidoStream.close();
		
		//Convertir el documento a PDF a bytes y cerramos el documento
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        	document.save(byteArrayOutputStream);
        	document.close();
        
        //regresamos el contenido del PDF en un arreglo de bytes
        return byteArrayOutputStream.toByteArray();
	}

}
