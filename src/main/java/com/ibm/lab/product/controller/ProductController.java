package com.ibm.lab.product.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.lab.product.dto.ProductDto;
import com.ibm.lab.product.model.Product;
import com.ibm.lab.product.service.ProductService;

@RestController
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Cacheable(value="product", key="#productCode")
    @GetMapping("/products/{productCode}")
    public ProductDto findByProductCode(@PathVariable String productCode) {
    	ProductDto productDto = productService.findByName(productCode);
    	logger.info("Products : {}", productDto);
    	
    	return Optional.ofNullable(productDto).orElseThrow(()->new RuntimeException("데이터가 없습니다."));
    	
    }
    
    @CachePut(value="product", key="#productDto.name")
    @PutMapping("/products")
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
    	logger.info("Products param: {}", productDto);
    	ProductDto prodDto = productService.updateProduct(productDto);
    	logger.info("Products : {}", productDto);
    	
    	return prodDto;
    }
    

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
    	List<Product> products = productService.findAll();
    	logger.info("Products : {}", products);
    	
    	return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
	@Cacheable(value="product", key="#productDto.name")
    @PostMapping("/products")
    public ProductDto saveProduct(@RequestBody ProductDto productDto) {
    	ProductDto prodDto = productService.saveProduct(productDto);
    	return prodDto;
    }
     
	@CacheEvict(value="product",key="#productName")
    @DeleteMapping("/products/{productName}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productName) {
    	productService.deleteProduct(productName);
    	
    	return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
