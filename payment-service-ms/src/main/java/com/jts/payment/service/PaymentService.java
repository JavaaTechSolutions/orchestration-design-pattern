package com.jts.payment.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jts.payment.dto.PaymentRequestDTO;
import com.jts.payment.dto.PaymentResponseDTO;
import com.jts.payment.dto.PaymentStatus;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {

	private Map<Integer, Double> paymentMap;

	@PostConstruct
	private void init() {
		paymentMap = new HashMap<>();

		paymentMap.put(1, 500d);
		paymentMap.put(2, 1000d);
		paymentMap.put(3, 700d);
	}
	
	public PaymentResponseDTO debit(PaymentRequestDTO requestDTO) {
		double balance = paymentMap.getOrDefault(requestDTO.getUserId(), 0d);
		
		PaymentResponseDTO responseDTO = new PaymentResponseDTO();
		responseDTO.setOrderId(requestDTO.getOrderId());
		responseDTO.setUserId(requestDTO.getUserId());
		responseDTO.setAmount(requestDTO.getAmount());
		responseDTO.setStatus(PaymentStatus.PAYMENT_REJECTED);
		
		System.out.println("Inside Payment::"+balance);
		
		if (balance >= requestDTO.getAmount()) {
			responseDTO.setStatus(PaymentStatus.PAYMENT_APPROVED);
			paymentMap.put(requestDTO.getUserId(), balance - requestDTO.getAmount());
		}
		
		return responseDTO;
	}
	
	public void credit(PaymentRequestDTO requestDTO) {
		paymentMap.computeIfPresent(requestDTO.getUserId(), (k, v) -> v + requestDTO.getAmount());
	}
}
