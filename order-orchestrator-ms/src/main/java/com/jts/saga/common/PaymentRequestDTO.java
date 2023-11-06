package com.jts.saga.common;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentRequestDTO {
    private Integer userId;
    private UUID orderId;
    private Double amount;
}
