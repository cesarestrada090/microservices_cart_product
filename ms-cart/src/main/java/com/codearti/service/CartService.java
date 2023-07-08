package com.codearti.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codearti.configuration.error.ResourceNotFoundException;
import com.codearti.model.dto.CartRequestDeleteDto;
import com.codearti.model.dto.CartRequestDto;
import com.codearti.model.dto.CartResponseDto;
import com.codearti.model.dto.ProductResponseDto;
import com.codearti.model.entity.CartEntity;
import com.codearti.model.entity.CartItemEntity;
import com.codearti.model.mapper.CartMapper;
import com.codearti.repository.CartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {
	
	private final CartRepository cartRepository;
	
	private final CartMapper mapper;
	
	private final ProductService productService;
	
	@Transactional(readOnly = true)
	public CartResponseDto findByCustomerId(Long customerId) {
		log.info("findByCustomerId");
		return cartRepository.findByCustomerId(customerId)
				.map(mapper::entityToResponse).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
	}
	
	@Transactional
	public CartResponseDto addItem(Long customerId, CartRequestDto cartRequestDto) {
		log.info("addItem");
		
		Optional<CartEntity> cartEntityResult = cartRepository.findByCustomerId(customerId);
		if(cartEntityResult.isEmpty()) {
			throw new ResourceNotFoundException("Resource not found");
		}
		
		CartEntity cartEntity = cartEntityResult.get();
		
		cartRequestDto.getItems().forEach(p -> {
			
			Optional<CartItemEntity> cartItemEntityOptional = cartEntity
					.getItems().stream().filter(pt -> pt.getProductId() == p.getProductId()).findFirst();
			
			if(!cartItemEntityOptional.isPresent()) {
				ProductResponseDto product = productService.findById(p.getProductId());
				if(product == null) {
					throw new ResourceNotFoundException("Product not found with id: " + p.getProductId());
				}else {
					log.info("ms-product port: " + product.getPort());
					cartEntity.getItems().add(mapper.responseToEntity(product, p.getQuantity()));
				}
			}
			
		});

		cartRepository.save(cartEntity);
		
		return mapper.entityToResponse(cartEntity);
	}
	
	@Transactional
	public CartResponseDto removeItem(Long customerId, CartRequestDeleteDto cartRequestDeleteDto) {
		log.info("deleteItem");
		
		Optional<CartEntity> cartEntityResult = cartRepository.findByCustomerId(customerId);
		if(cartEntityResult.isEmpty()) {
			throw new ResourceNotFoundException("Resource not found");
		}
		
		CartEntity cartEntity = cartEntityResult.get();
		
		cartRequestDeleteDto.getItems().forEach(p -> {
			cartEntity.getItems().removeIf(pt -> pt.getProductId() == p.getProductId());
		});

		cartRepository.save(cartEntity);
		
		return mapper.entityToResponse(cartEntity);
	}

}
