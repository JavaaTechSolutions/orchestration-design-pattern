package com.jts.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jts.order.dto.OrchestratorRequestDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class Config {
	
	@Bean
	public Sinks.Many<OrchestratorRequestDTO> sink() {
		return Sinks.many().unicast().onBackpressureBuffer();
	}

	@Bean
	public Flux<OrchestratorRequestDTO> flux(Sinks.Many<OrchestratorRequestDTO> sink) {
		return sink.asFlux();
	}
}
