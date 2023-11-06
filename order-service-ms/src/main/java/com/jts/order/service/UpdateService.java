package com.jts.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jts.order.dto.OrchestratorResponseDTO;
import com.jts.order.repo.PurchaseOrderRespository;

import reactor.core.publisher.Mono;

@Service
public class UpdateService {

	@Autowired
	private PurchaseOrderRespository repo;
	
	public Mono<Void> updateOrder(OrchestratorResponseDTO responseDTO) {
		System.out.println("Response::"+responseDTO.getStatus());
		
		return repo.findById(responseDTO.getOrderId())
		.doOnNext(p -> p.setStatus(responseDTO.getStatus()))
		.doOnNext(repo::save)
		.then();
	}
}
