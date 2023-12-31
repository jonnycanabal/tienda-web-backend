package com.tienda.web.app.services;

import java.util.Optional;

import com.tienda.web.app.models.entity.Brand;


public interface BrandService {


	public Iterable<Brand> finAll();

	public Optional<Brand> finbyId(Long id);

	public Brand save(Brand brand);

	public void deleteById(Long id);
}
