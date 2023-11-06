package com.jts.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jts.order.dto.OrderRequestDTO;
import com.jts.order.dto.OrderResponseDTO;
import com.jts.order.entity.PurchaseOrder;
import com.jts.order.service.OrderService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/create")
	public Mono<PurchaseOrder> createOrder(@RequestBody Mono<OrderRequestDTO> orderMono) {
		return orderMono.flatMap(orderService::createOrder);
	}
	
	@GetMapping("/all")
	public Flux<OrderResponseDTO> getOrders() {
		return orderService.getAllOrder();
	}
	
}
