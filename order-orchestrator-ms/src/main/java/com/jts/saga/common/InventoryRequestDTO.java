package com.jts.saga.common;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InventoryRequestDTO {
	private Integer userId;
	private Integer productId;
	private UUID orderId;

}
