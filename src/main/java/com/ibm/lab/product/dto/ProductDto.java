package com.ibm.lab.product.dto;

import com.ibm.lab.product.model.Product;

public class ProductDto {
	private String name;
	private String description;
	private Long price;
    private String category;
    
    public ProductDto() {}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
    @Override
    public String toString() {
        return "Payment{" +
                ", name=" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", category='" + category  +
                '}';
    }
}
