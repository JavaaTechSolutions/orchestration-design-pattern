package com.jts.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jts.payment.dto.PaymentRequestDTO;
import com.jts.payment.dto.PaymentResponseDTO;
import com.jts.payment.service.PaymentService;

@RestController
@RequestMapping("payment")
public class PaymentControler {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/debit")
	public PaymentResponseDTO debit(@RequestBody PaymentRequestDTO requestDTO) {
		return paymentService.debit(requestDTO);
	}
	
	@PostMapping("/credit")
	public void credit(@RequestBody PaymentRequestDTO requestDTO) {
		paymentService.credit(requestDTO);
	}

}
