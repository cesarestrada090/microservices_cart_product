package com.codearti.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.codearti.client.ProductClientRest;
import com.codearti.model.dto.ProductResponseDto;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class ProductServiceFeignImpl implements ProductService {
	
	private final ProductClientRest client;

	@Override
	public List<ProductResponseDto> findAll() {
		log.info("findAll");
		return client.findAll();
	}

	@Override
	public ProductResponseDto findById(Long id) {
		log.info("findById feign");
		try {
			return client.findById(id);
		}catch (FeignException e) {
			if(e.status() == HttpStatus.NOT_FOUND.value()) {
				return null;
			}else {
				throw e;
			}
		}
	}

}
