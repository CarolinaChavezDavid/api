package com.accenture.api.controllers.dto;

public  class OrderPoductDTO {

    private long idProduct;
        
    private int quantity;
        
    public long getIdProduct() {
            return idProduct;
        }
    
        public void setIdProduct(long idProduct) {
            this.idProduct = idProduct;
        }
    
    public int getQuantity() {
            return quantity;
        }
    public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    @Override
    public String toString() {
            return "OrderPoductDTO [idProduct=" + idProduct + ", quantity=" + quantity + "]";
        }    
}