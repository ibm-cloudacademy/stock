package com.ibm.lab.stock.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StockDto {
    private String orderId;
    private String productCode;
    private String adjustmentType;
    private Long qty;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productId) {
        this.productCode = productId;
    }

    public static StockDto deserializeJSON(final String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, StockDto.class);
    }

    public String serializeJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
    
    @Override
    public String toString() {
        return "Stock{" +
                "orderId=" + orderId +
                ", productCode='" + productCode + '\'' +
                ", qty=" + qty +
                '}';
    }
	
}
