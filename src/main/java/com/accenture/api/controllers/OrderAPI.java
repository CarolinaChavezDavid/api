package com.accenture.api.controllers;

import java.util.List;

import com.accenture.api.controllers.dto.OrderDTO;
import com.accenture.api.exceptions.OrderException;
import com.accenture.api.models.Order;
import com.accenture.api.services.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")

public class OrderAPI {

    Logger logger = LoggerFactory.getLogger(OrderAPI.class);

    @Autowired
    OrderService orderService;

    @GetMapping()
    public ResponseEntity<List<Order>> getOrders(){
        return ResponseEntity.ok().body(orderService.getOrders());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(order);
        } catch (Exception e) {
            logger.error("Error in create order", e);
           return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO){
        try {
            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok().body(order);
        } catch (Exception e) {
            logger.error("Error in update order", e);
            return ResponseEntity.badRequest().build();
        }
        
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) throws OrderException{
        try {
            return ResponseEntity.ok().body(orderService.orderDeleteById(id));
        } catch (Exception e) {
            logger.error("Error in delete order", e);
            return ResponseEntity.badRequest().build();
        }
        
    }
}
