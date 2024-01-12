package com.tienda.web.app.models.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tienda.web.app.models.entity.Brand;

public interface BrandRepository extends CrudRepository<Brand, Long> {
	
	List<Brand> findByBrandNameContainingIgnoreCase(String brandName);

}
