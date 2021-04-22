package com.accenture.api.controllers.dto;

import java.util.List;

public class OrderDTO{

    private List<OrderPoductDTO> productList;
    private long idClient;

    public List<OrderPoductDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<OrderPoductDTO> productList) {
        this.productList = productList;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    } 

    @Override
    public String toString() {
        return "OrderDTO [idClient=" + idClient + ", productList=" + productList + "]";
    }
   
}
