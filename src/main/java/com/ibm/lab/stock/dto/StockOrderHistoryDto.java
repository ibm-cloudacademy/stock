package com.ibm.lab.stock.dto;

public class StockOrderHistoryDto {
	private String productName;
	
	private String adjustmentType;

	private Long qty;

	private String orderId;

	public StockOrderHistoryDto() {}

	public StockOrderHistoryDto(Long stockId, String orderId, String adjustmentType, Long qty) {
		this.productName = productName;
		this.adjustmentType = adjustmentType;
		this.qty = qty;
		this.orderId = orderId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName= productName;
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
}
