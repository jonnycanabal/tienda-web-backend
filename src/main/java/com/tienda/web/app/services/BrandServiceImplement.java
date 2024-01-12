package com.tienda.web.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.web.app.models.entity.Brand;
import com.tienda.web.app.models.repository.BrandRepository;

@Service
public class BrandServiceImplement implements BrandService {

	@Autowired
	private BrandRepository repository;

	@Override
	public Iterable<Brand> finAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Brand> finbyId(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Brand save(Brand brand) {
		return repository.save(brand);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Override
	public List<Brand> findByBrandNameContainingIgnoreCase(String brandName) {

		return repository.findByBrandNameContainingIgnoreCase(brandName);
	}

}
