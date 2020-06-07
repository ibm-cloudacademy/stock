package com.ibm.lab.product.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.lab.product.model.Product;

@Repository
public class ProductRepository {
	private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	public Product findByProductName(String productName) {
		Product product = sqlSessionTemplate.selectOne("product.findByProductName", productName);
		
		return product;
	}
	
	public List<Product> findAll(Long limit) {
		List<Product> products = sqlSessionTemplate.selectList("product.findAll", limit);
		
		return products;
	}
	
	public Product findById(Long id) {
		Product product = sqlSessionTemplate.selectOne("product.findById", id);
		
		return product;
	}
	
	public void saveProduct(Product product) {
		sqlSessionTemplate.insert("product.saveProduct", product);
	}
	
	public void updateProduct(Product product) {
		sqlSessionTemplate.update("product.updateProduct", product);
	}
	
	public void deleteProduct(Long productId) {
		sqlSessionTemplate.delete("product.deleteProduct", productId);
	}
}
