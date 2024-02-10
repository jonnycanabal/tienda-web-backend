package com.tienda.web.app.service.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.tienda.web.app.models.entity.ShoppingCart;
import com.tienda.web.app.models.repository.ShoppingCartRepository;
import com.tienda.web.app.services.ShoppingCartServiceImplement;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ShoppingCartServiceTest {
	
	@Mock
	private ShoppingCartRepository repository;
	
	@InjectMocks
	private ShoppingCartServiceImplement service;
	
	private ShoppingCart shoppingCart;
	
	@BeforeEach
	public void cartTest() {
		
		shoppingCart = new ShoppingCart();
		
		shoppingCart.setId((long) 1);
		
	}
	
	@Test
	public void testFinAll() {
		
		List<ShoppingCart> cartList = Arrays.asList(shoppingCart);
		
		Mockito.when(repository.findAll()).thenReturn(cartList);
		
		Iterable<ShoppingCart> listResult = service.finAll();
		
		assertEquals(cartList, listResult);
	}
	
	@Test
	public void testFinById() {
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(shoppingCart));
		
		Optional<ShoppingCart> response = service.finById(shoppingCart.getId());
		
		assertTrue(response.isPresent());
		
		assertEquals(shoppingCart.getId(), response.get().getId());
	}
	
	@Test
	public void testSaveShoppingCart() {
		
		Mockito.when(repository.save(Mockito.any(ShoppingCart.class))).thenReturn(shoppingCart);
		
		ShoppingCart result = service.save(shoppingCart);
		
		assertNotNull(result);
		
	}
	
	@Test
	public void testDeleteShoppingCart() {
		
		Long ShoppingCartId = shoppingCart.getId();
		
		service.deleteById(ShoppingCartId);
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(ShoppingCartId);
	}
	
//	@Test
//	public void testInvoice() throws IOException{
//		
//		Long invoiceId = shoppingCart.getId();
//		
//		byte[] fakeInvoice = "Datos simulados".getBytes();
//		
//		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(shoppingCart));
//		
//		Mockito.when(service.generateInvoice(Mockito.anyLong())).thenReturn(fakeInvoice);
//		
//		String result = service.invoiceContent(invoiceId);
//		
//		assertNotNull(result);
//		
//	}

}
