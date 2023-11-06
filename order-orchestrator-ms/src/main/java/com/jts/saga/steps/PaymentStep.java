package com.jts.saga.steps;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.jts.saga.common.PaymentRequestDTO;
import com.jts.saga.common.PaymentResponseDTO;
import com.jts.saga.common.PaymentStatus;
import com.jts.saga.service.WorkflowStep;
import com.jts.saga.service.WorkflowStepStatus;

import reactor.core.publisher.Mono;

public class PaymentStep implements WorkflowStep {

    private final WebClient webClient;
    private final PaymentRequestDTO requestDTO;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public PaymentStep(WebClient webClient, PaymentRequestDTO requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        return webClient
                    .post()
                    .uri("/payment/debit")
                    .body(BodyInserters.fromValue(requestDTO))
                    .retrieve()
                    .bodyToMono(PaymentResponseDTO.class)
                    .map(r -> r.getStatus().equals(PaymentStatus.PAYMENT_APPROVED))
                    .doOnNext(b -> stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        return this.webClient
                .post()
                .uri("/payment/credit")
                .body(BodyInserters.fromValue(requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r -> true)
                .onErrorReturn(false);
    }

}
