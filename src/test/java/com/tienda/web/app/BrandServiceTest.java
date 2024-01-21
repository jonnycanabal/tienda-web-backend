package com.tienda.web.app;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.tienda.web.app.models.entity.Brand;
import com.tienda.web.app.models.repository.BrandRepository;
import com.tienda.web.app.services.BrandServiceImplement;

//import io.jsonwebtoken.lang.Collections;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {
	
	@Mock
	private BrandRepository repository;
	
	@InjectMocks
	private BrandServiceImplement service;
	
	private Brand brand;
	
	@BeforeEach
	public void testBrand() {
		
		brand = new Brand();
		
		brand.setId((long) 1);
		brand.setBrandName("Adidas");
		
	}
	
	@Test
	public void testFinAll() {
		
		Set<Brand> brandList = Set.of(brand);
		
		Mockito.when(repository.findAll()).thenReturn(brandList);
		
		Iterable<Brand> result = service.finAll();
		
		assertEquals(brandList, result);
		
	}
	
	@Test
	public void testFinById() {
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(brand));
		
		Optional<Brand> result = service.finbyId((long) 1);
		
		assertTrue(result.isPresent());
		assertEquals(brand.getId(), result.get().getId());
	}
	
	@Test
	public void testSaveBrand() {
		
		Mockito.when(repository.save(Mockito.any(Brand.class))).thenReturn(brand);
		
		Brand result = service.save(brand);
		
		assertNotNull(result);
		assertEquals(brand.getId(), result.getId());
		
	}
	
	@Test
	public void testDeleteBrand() {
		
		Long brandToDelete = (long) 1;
		
		service.deleteById(brandToDelete);
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(brandToDelete);
				
	}
	
	@Test
	public void testFindByName() {
		
		Mockito.when(repository.findByBrandNameContainingIgnoreCase(Mockito.anyString())).thenReturn(List.of(brand));
		
		List<Brand> brandList = service.findByBrandNameContainingIgnoreCase("Adidas");
		
		assertNotNull(brandList);
		assertEquals(1, brandList.size());
		
		Brand brandPositionList = brandList.get(0);
		
		assertEquals(brand.getBrandName(), brandPositionList.getBrandName());
	}
	
//	@Test
//	public void testFindByName_NoExistingName() {
//		
//		Mockito.when(repository.findByBrandNameContainingIgnoreCase(Mockito.anyString())).thenReturn(Collections.emptyList());
//		
//		List<Brand> brandList = service.findByBrandNameContainingIgnoreCase("No Existe");
//		
//		assertNotNull(brandList);
//		assertTrue(brandList.isEmpty());
//	}

}
