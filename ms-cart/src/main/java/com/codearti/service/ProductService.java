package com.codearti.service;

import java.util.List;

import com.codearti.model.dto.ProductResponseDto;

public interface ProductService {

	public List<ProductResponseDto> findAll();
	public ProductResponseDto findById(Long id);
}
