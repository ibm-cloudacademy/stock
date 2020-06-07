package com.ibm.lab.stock.model;

public class StockOrderHistory {
	private Long productId;

	private String adjustmentType;

	private Long qty;

	private String orderId;

	public StockOrderHistory() {
	}

	public StockOrderHistory(Long productId, String orderId, String adjustmentType, Long qty) {
		this.productId = productId;
		this.adjustmentType = adjustmentType;
		this.qty = qty;
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
    @Override
    public String toString() {
        return "Stock{" +
                "orderId=" + orderId +
                ", adjustmentType='" + adjustmentType + '\'' +
                ", productId='" + productId + '\'' +
                ", qty=" + qty +
                '}';
    }
}
