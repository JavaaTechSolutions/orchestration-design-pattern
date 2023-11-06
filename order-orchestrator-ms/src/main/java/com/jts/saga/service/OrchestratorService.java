package com.jts.saga.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jts.saga.common.InventoryRequestDTO;
import com.jts.saga.common.OrchestratorRequestDTO;
import com.jts.saga.common.OrchestratorResponseDTO;
import com.jts.saga.common.OrderStatus;
import com.jts.saga.common.PaymentRequestDTO;
import com.jts.saga.steps.InventoryStep;
import com.jts.saga.steps.PaymentStep;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorService {

	@Autowired
	@Qualifier("payment")
	private WebClient paymentClient;

	@Autowired
	@Qualifier("inventory")
	private WebClient inventoryClient;

	public Mono<OrchestratorResponseDTO> orderProduct(final OrchestratorRequestDTO requestDTO) {
		Workflow orderWorkflow = getOrderWorkflow(requestDTO);

		return Flux.fromStream(() -> orderWorkflow.getSteps().stream()).flatMap(WorkflowStep::process)
				.handle(((aBoolean, synchronousSink) -> {
					if (aBoolean.booleanValue()) {
						synchronousSink.next(true);
					} else {
						synchronousSink.error(new WorkflowException("Order not processed."));
					}
				})).then(Mono.fromCallable(() -> getResponseDTO(requestDTO, OrderStatus.ORDER_COMPLETED)))
				.onErrorResume(ex -> revertOrder(orderWorkflow, requestDTO));

	}

	private Mono<OrchestratorResponseDTO> revertOrder(final Workflow workflow, final OrchestratorRequestDTO requestDTO) {
		return Flux.fromStream(() -> workflow.getSteps().stream())
				.filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
				.flatMap(WorkflowStep::revert).retry(3)
				.then(Mono.just(getResponseDTO(requestDTO, OrderStatus.ORDER_CANCELLED)));
	}

	private Workflow getOrderWorkflow(OrchestratorRequestDTO requestDTO) {
		WorkflowStep paymentStep = new PaymentStep(paymentClient, getPaymentRequestDTO(requestDTO));
		WorkflowStep inventoryStep = new InventoryStep(inventoryClient, getInventoryRequestDTO(requestDTO));
		return new OrderWorkflow(List.of(paymentStep, inventoryStep));
	}

	private OrchestratorResponseDTO getResponseDTO(OrchestratorRequestDTO requestDTO, OrderStatus status) {
		OrchestratorResponseDTO responseDTO = new OrchestratorResponseDTO();
		responseDTO.setOrderId(requestDTO.getOrderId());
		responseDTO.setAmount(requestDTO.getAmount());
		responseDTO.setProductId(requestDTO.getProductId());
		responseDTO.setUserId(requestDTO.getUserId());
		responseDTO.setStatus(status);
		return responseDTO;
	}

	private PaymentRequestDTO getPaymentRequestDTO(OrchestratorRequestDTO requestDTO) {
		PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
		paymentRequestDTO.setUserId(requestDTO.getUserId());
		paymentRequestDTO.setAmount(requestDTO.getAmount());
		paymentRequestDTO.setOrderId(requestDTO.getOrderId());
		return paymentRequestDTO;
	}

	private InventoryRequestDTO getInventoryRequestDTO(OrchestratorRequestDTO requestDTO) {
		InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO();
		inventoryRequestDTO.setUserId(requestDTO.getUserId());
		inventoryRequestDTO.setProductId(requestDTO.getProductId());
		inventoryRequestDTO.setOrderId(requestDTO.getOrderId());
		return inventoryRequestDTO;
	}

}
