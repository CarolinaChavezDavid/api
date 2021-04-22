package com.accenture.api.services;

import java.util.List;
import java.util.Optional;

import com.accenture.api.exceptions.OrderException;
import com.accenture.api.models.Product;
import com.accenture.api.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired 
    ProductRepository productRepository;

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id) throws OrderException{
        Optional<Product> productOptional =  productRepository.findById(id);
        if(productOptional.isPresent()){
            return productOptional.get();
        }else{
            throw new OrderException("The product does not exist");
        }
    }
}
