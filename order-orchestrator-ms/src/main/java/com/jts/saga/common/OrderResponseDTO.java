package com.jts.saga.common;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderResponseDTO {
    private UUID orderId;
    private Integer userId;
    private Integer productId;
    private Double amount;
    private OrderStatus status;
}
