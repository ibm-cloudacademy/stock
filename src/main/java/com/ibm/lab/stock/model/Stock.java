package com.ibm.lab.stock.model;

import java.util.ArrayList;
import java.util.List;

public class Stock {

    private Long id;

    private Long productId;

    private Long qty;

    public Stock() {
    }

    public Stock(Long id, Long productId, Long availableStockQty) {
    	this.id = id;
        this.productId = productId;
        this.qty = availableStockQty;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
    
    public Long getQty() {
        return qty;
    }

    public void decrease(final Long qty) {
        this.qty = this.qty - qty;
    }

    public void increase(final Long qty) {
        this.qty = this.qty + qty;
    }


    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", qty=" + qty +
                '}';
    }
}
