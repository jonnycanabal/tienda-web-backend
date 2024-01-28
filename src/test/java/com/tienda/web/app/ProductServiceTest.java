package com.tienda.web.app;

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

import com.tienda.web.app.models.entity.Product;
import com.tienda.web.app.models.repository.ProductRepository;
import com.tienda.web.app.services.ProductServiceImplement;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductServiceTest {
	
	@Mock
	private ProductRepository repository;
	
	@InjectMocks
	private ProductServiceImplement service;
	
	private Product product;
	
	@BeforeEach
	public void testProduct() {
		
		product = new Product();
		
		product.setId((long) 1);
		product.setProductName("AllStar");
		product.setPrice(200000);
		
	}
	
	@Test
	public void testFinAll() {
		
		List<Product> productList = Arrays.asList(product);
		
		Mockito.when(repository.findAll()).thenReturn(productList);
		
		Iterable<Product> result = service.finAll();
		
		assertEquals(productList, result);
	}
	
	@Test
	public void testFinById() {
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
		
		Optional<Product> response = service.finById((long) 1);
		
		assertTrue(response.isPresent());
		assertEquals(product.getId(), response.get().getId());
	}
	
	@Test
	public void testSaveProduct() {
		
		Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(product);
		
		Product result = service.save(product);
		
		assertNotNull(result);
		assertEquals(product.getId(), result.getId());	
	}
	
	@Test
	public void testDeletedProduct() {
		Long productToDelete = (long) 1;
		
		service.deleteById(productToDelete);
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(productToDelete);
		
	}
	
	@Test
	public void testFindByProductName() {
		
		Mockito.when(repository.findByProductNameContainingIgnoreCase(Mockito.anyString())).thenReturn(Arrays.asList(product));
		
		List<Product> response = service.findByProductNameContainingIgnoreCase("AllStar");
		
		assertNotNull(response);
		assertEquals(1, response.size());
		
		Product productPositionList = response.get(0);
		
		assertEquals(product.getProductName(), productPositionList.getProductName());
	}

}
