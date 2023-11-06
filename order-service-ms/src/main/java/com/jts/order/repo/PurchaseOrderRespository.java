package com.jts.order.repo;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.jts.order.entity.PurchaseOrder;

@Repository
public interface PurchaseOrderRespository extends ReactiveCrudRepository<PurchaseOrder, UUID> {

}
