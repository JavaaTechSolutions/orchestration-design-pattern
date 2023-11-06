package com.jts.inventory.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jts.inventory.dto.InventoryRequestDTO;
import com.jts.inventory.dto.InventoryResponseDTO;
import com.jts.inventory.dto.InventoryStatus;

import jakarta.annotation.PostConstruct;

@Service
public class InventoryService {

	private Map<Integer, Integer> inventoryMap;

	@PostConstruct
	private void init() {
		inventoryMap = new HashMap<>();

		inventoryMap.put(1, 2);
		inventoryMap.put(2, 3);
		inventoryMap.put(3, 4);
	}
	
	public InventoryResponseDTO deduct(InventoryRequestDTO requestDTO) {
		int qty = inventoryMap.getOrDefault(requestDTO.getProductId(), 0);
		
		InventoryResponseDTO responseDTO = new InventoryResponseDTO();
		responseDTO.setOrderId(requestDTO.getOrderId());
		responseDTO.setProductId(requestDTO.getProductId());
		responseDTO.setUserId(requestDTO.getUserId());
		responseDTO.setStatus(InventoryStatus.UNAVAILABLE);
		
		System.out.println("Inside Inventory::"+qty);
		
		if (qty > 0) {
			responseDTO.setStatus(InventoryStatus.AVAILABLE);
			inventoryMap.put(requestDTO.getProductId(), qty - 1);
		}
		
		return responseDTO;
	}
	
	public void add(InventoryRequestDTO requestDTO) {
		inventoryMap.computeIfPresent(requestDTO.getProductId(), (k, v) -> v + 1);
	}
}
