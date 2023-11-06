package com.jts.order.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrchestratorResponseDTO {

	private Integer userId;
	private Integer productId;
	private UUID orderId;
	private Double amount;
	private OrderStatus status;

}
