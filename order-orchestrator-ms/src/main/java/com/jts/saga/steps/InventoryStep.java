package com.jts.saga.steps;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.jts.saga.common.InventoryRequestDTO;
import com.jts.saga.common.InventoryResponseDTO;
import com.jts.saga.common.InventoryStatus;
import com.jts.saga.service.WorkflowStep;
import com.jts.saga.service.WorkflowStepStatus;

import reactor.core.publisher.Mono;

public class InventoryStep implements WorkflowStep {

    private final WebClient webClient;
    private final InventoryRequestDTO requestDTO;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public InventoryStep(WebClient webClient, InventoryRequestDTO requestDTO) {
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
                .uri("/inventory/deduct")
                .body(BodyInserters.fromValue(requestDTO))
                .retrieve()
                .bodyToMono(InventoryResponseDTO.class)
                .map(r -> r.getStatus().equals(InventoryStatus.AVAILABLE))
                .doOnNext(b -> stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        return webClient
                    .post()
                    .uri("/inventory/add")
                    .body(BodyInserters.fromValue(requestDTO))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .map(r -> true)
                    .onErrorReturn(false);
    }
}
