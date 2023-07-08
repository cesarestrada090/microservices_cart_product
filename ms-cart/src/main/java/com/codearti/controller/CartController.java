package com.codearti.controller;

import javax.validation.Valid;

import com.codearti.model.dto.CartRequestDeleteDto;
import com.codearti.model.dto.CartRequestDto;
import com.codearti.model.dto.CartResponseDto;
import com.codearti.service.CartService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

//@RefreshScope
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v1")
public class CartController {
	
	private final CartService service;

	@GetMapping(value = "/{customerId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<CartResponseDto> findByCustomerId(@PathVariable Long customerId){
		CartResponseDto result = service.findByCustomerId(customerId);
		return ResponseEntity.ok(result);
	}
	
	@PostMapping(value = "/{customerId}/item", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE }, 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<CartResponseDto> addItem(@PathVariable Long customerId, 
												   @Valid @RequestBody CartRequestDto cartRequestDto) {
		CartResponseDto result = service.addItem(customerId, cartRequestDto);
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping(value = "/{customerId}/item", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE }, 
	consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<CartResponseDto> removeItem(@PathVariable Long customerId, 
											   		  @Valid @RequestBody CartRequestDeleteDto cartRequestDeleteDto) {
		CartResponseDto result = service.removeItem(customerId, cartRequestDeleteDto);
		return ResponseEntity.ok(result);
	}

}