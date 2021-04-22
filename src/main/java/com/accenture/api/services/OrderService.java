package com.accenture.api.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.accenture.api.controllers.dto.OrderDTO;
import com.accenture.api.controllers.dto.OrderPoductDTO;
import com.accenture.api.exceptions.OrderException;
import com.accenture.api.models.Client;
import com.accenture.api.models.Order;
import com.accenture.api.models.OrderDetail;
import com.accenture.api.models.Product;
import com.accenture.api.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired 
    OrderRepository orderRepository;

    @Autowired 
    ClientService clientService;

    @Autowired 
    ProductService productService;

    @Autowired
    OrderDetailService orderDetailService;

    public static final BigDecimal VALUE_WITHOUT_DELIVERY = new BigDecimal(100000);

    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

    public Order createOrder(OrderDTO orderDTO) throws OrderException{
        
        Order order = new Order();
        Client cliente = clientService.getById(orderDTO.getIdClient());
        order.setClient(cliente);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        BigDecimal subtotalPrice = calculateSubtotal( orderDTO.getProductList(), orderDetailList);

        BigDecimal ivaValue = subtotalPrice.multiply(new BigDecimal(0.19));
        
        order.setSubTotal(subtotalPrice);
        order.setIva(ivaValue);
        order.setTotal(subtotalPrice.add(ivaValue));
        order.setCreationTime(LocalDateTime.now());
        order.setStatus("Active");
        BigDecimal deliveryCost = getDeliveryCost(order.getTotal());
        order.setDelivery(deliveryCost);
        saveOrder(order);

        for(OrderDetail orderdDetail : orderDetailList){
            orderdDetail.setOrder(order);
            orderDetailService.createOrderDetail(orderdDetail);
        }
        return order;
    }

    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO) throws OrderException{
        Order order = findOrderById(id);
        LocalDateTime updateTime = order.getCreationTime().plusHours(5);

        if(LocalDateTime.now().isBefore(updateTime)){
            
            List<OrderDetail> orderDetailList = new ArrayList<>();
            BigDecimal subtotalPrice = calculateSubtotal( orderDTO.getProductList(), orderDetailList);
         
            int compare = subtotalPrice.compareTo(order.getSubTotal());
            if( compare == 0 || compare==1){
                BigDecimal ivaValue = subtotalPrice.multiply(new BigDecimal(0.19));
        
                order.setSubTotal(subtotalPrice);
                order.setIva(ivaValue);
                order.setTotal(subtotalPrice.add(ivaValue));
                order.setStatus("update");
                BigDecimal deliveryCost = getDeliveryCost(order.getTotal());
                order.setDelivery(deliveryCost);
                saveOrder(order);

                orderDetailService.deleteByOrderId(id);

                for(OrderDetail orderdDetail : orderDetailList){
                    orderdDetail.setOrder(order);
                    orderDetailService.createOrderDetail(orderdDetail);
                }

                return order;
                
            }else{
                throw new OrderException("The price of the order is not enough for updating");
            }
            
        }else{
            throw new OrderException("The updating time has expired");
        }
    }

    @Transactional
    public String orderDeleteById(Long id) throws OrderException{
        Order order = findOrderById(id);
        LocalDateTime deleteTime = order.getCreationTime().plusHours(12);

        if(LocalDateTime.now().isBefore(deleteTime)){
            orderDetailService.deleteByOrderId(id);
            orderRepository.deleteById(id);
            return "The order with id " + id +  " was deleted";
        }else{
            order.setStatus("Canceled");
            Order orderAnnulment = new Order();
            BigDecimal totalPriceAnnulment = order.getTotal().multiply(new BigDecimal(0.1));

            orderAnnulment.setClient(order.getClient());
            orderAnnulment.setCreationTime(LocalDateTime.now());
            orderAnnulment.setDelivery(new BigDecimal(0));
            orderAnnulment.setIva(new BigDecimal(0));
            orderAnnulment.setSubTotal(totalPriceAnnulment);
            orderAnnulment.setTotal(totalPriceAnnulment);
            orderAnnulment.setStatus("Active");

            saveOrder(orderAnnulment);
            saveOrder(order);

            return "The order wasn't delete, new order was created";
        }
    }

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }

    public Order findOrderById(Long id) throws OrderException{
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isPresent()){
            return orderOptional.get();
        }else{
            throw new OrderException("The Order does not exist");
        }
    }

    private BigDecimal calculateSubtotal(List<OrderPoductDTO> orderPoductDTOList, List<OrderDetail> orderDetailList) throws OrderException{
        BigDecimal subtotalPrice = new BigDecimal(0);

        for(OrderPoductDTO orderPoductDTO : orderPoductDTOList){
    
            Product product =  productService.getProductById(orderPoductDTO.getIdProduct());
            BigDecimal price = product.getPrice();
            int quantity = orderPoductDTO.getQuantity();
            orderDetailList.add(new OrderDetail(product, quantity));
            subtotalPrice = subtotalPrice.add(price.multiply(new BigDecimal(quantity)));
        }

        return subtotalPrice;

    }

    private BigDecimal getDeliveryCost(BigDecimal totalPrice){

        int compare = totalPrice.compareTo(VALUE_WITHOUT_DELIVERY);

        if(compare==0 || compare==1){
            return new BigDecimal(0);
        }else{
            return new BigDecimal(5000);
        }
    }
    
   
}
