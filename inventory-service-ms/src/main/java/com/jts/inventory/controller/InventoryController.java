package com.jts.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jts.inventory.dto.InventoryRequestDTO;
import com.jts.inventory.dto.InventoryResponseDTO;
import com.jts.inventory.service.InventoryService;

@RestController
@RequestMapping("inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;

	@PostMapping("/deduct")
	public InventoryResponseDTO deduct(@RequestBody InventoryRequestDTO rqRequestDTO) {
		return inventoryService.deduct(rqRequestDTO);
		
	}
	
	@PostMapping("/add")
	public void add(@RequestBody InventoryRequestDTO rqRequestDTO) {
		inventoryService.add(rqRequestDTO);
	}
}
