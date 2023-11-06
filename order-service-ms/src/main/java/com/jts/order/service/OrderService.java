package com.jts.order.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jts.order.dto.OrchestratorRequestDTO;
import com.jts.order.dto.OrderRequestDTO;
import com.jts.order.dto.OrderResponseDTO;
import com.jts.order.dto.OrderStatus;
import com.jts.order.entity.PurchaseOrder;
import com.jts.order.repo.PurchaseOrderRespository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class OrderService {

	private static final Map<Integer, Double> ORDER_PRICE = Map.of(1, 100d, 
			2, 200d, 
			3, 300d);

	@Autowired
	private PurchaseOrderRespository repository;

	@Autowired
	private Sinks.Many<OrchestratorRequestDTO> sink;

	public Mono<PurchaseOrder> createOrder(OrderRequestDTO orderRequestDTO) {
		return repository.save(dtoToEntity(orderRequestDTO))
				.doOnNext(e -> orderRequestDTO.setOrderId(e.getId()))
				.doOnNext(e -> emitEvent(orderRequestDTO));
	}

	public Flux<OrderResponseDTO> getAllOrder() {
		return repository.findAll()
		.map(this::entityToDto);
	}

	private void emitEvent(OrderRequestDTO orderRequestDTO) {
		sink.tryEmitNext(getOrchestratorRequestDTO(orderRequestDTO));
	}

	private PurchaseOrder dtoToEntity(final OrderRequestDTO dto) {
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setId(dto.getOrderId());
		purchaseOrder.setProductId(dto.getProductId());
		purchaseOrder.setUserId(dto.getUserId());
		purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
		purchaseOrder.setPrice(ORDER_PRICE.get(purchaseOrder.getProductId()));
		return purchaseOrder;
	}

	public OrchestratorRequestDTO getOrchestratorRequestDTO(OrderRequestDTO orderRequestDTO) {
		OrchestratorRequestDTO requestDTO = new OrchestratorRequestDTO();
		requestDTO.setUserId(orderRequestDTO.getUserId());
		requestDTO.setAmount(ORDER_PRICE.get(orderRequestDTO.getProductId()));
		requestDTO.setOrderId(orderRequestDTO.getOrderId());
		requestDTO.setProductId(orderRequestDTO.getProductId());
		return requestDTO;
	}

	private OrderResponseDTO entityToDto(PurchaseOrder purchaseOrder) {
		System.out.println("Purchase Order Status::"+purchaseOrder.getStatus());
		OrderResponseDTO dto = new OrderResponseDTO();
		dto.setOrderId(purchaseOrder.getId());
		dto.setProductId(purchaseOrder.getProductId());
		dto.setUserId(purchaseOrder.getUserId());
		dto.setStatus(purchaseOrder.getStatus());
		dto.setAmount(purchaseOrder.getPrice());
		return dto;
	}
}
