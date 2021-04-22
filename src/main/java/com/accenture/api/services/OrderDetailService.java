package com.accenture.api.services;

import com.accenture.api.models.OrderDetail;
import com.accenture.api.repositories.OrderDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;

    public OrderDetail createOrderDetail(OrderDetail orderdDetail) {
        return orderDetailRepository.save(orderdDetail);
    }

    public void deleteByOrderId(Long id){
        orderDetailRepository.deleteByOrderId(id);
    }
       
}
