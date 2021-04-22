package com.accenture.api.repositories;

import com.accenture.api.models.OrderDetail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{
    void deleteByOrderId(Long orderId);
}
