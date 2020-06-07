package com.ibm.lab.product.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.lab.product.dto.ProductDto;
import com.ibm.lab.product.model.Product;
import com.ibm.lab.product.repository.ProductRepository;
import com.ibm.lab.stock.model.Stock;
import com.ibm.lab.stock.service.StockService;

@Service
public class ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	@Value("${academy.stock-max-count}")
	private Long stockMaxCount;
	
	@Value("${academy.stock-qty}")
	private Long stockQty;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private StockService stockService;
	
	/**
	 * 상품  조회  
	 * @return
	 */
	public List<Product> findAll() {
		return productRepository.findAll(stockMaxCount);
	}
	
	/**
	 * 상품 id 조회  
	 * @return
	 */
	public Product findById(Long productId) {
		return productRepository.findById(productId);
	}
	
	/**
	 * 상품 id 조회  
	 * @return
	 */
	public ProductDto findByName(String productCode) {
		Product product = productRepository.findByProductName(productCode);
		
		logger.info("findByName :{}", product, productCode);
		ProductDto productDto = new ProductDto();
		productDto.setCategory(product.getCategory());
		productDto.setDescription(product.getDescription());
		productDto.setName(product.getName());
		productDto.setPrice(productDto.getPrice());
		
		return productDto;
	}
	
	/**
	 * 상품 수정 
	 * @param stockDto
	 */
	@Transactional
	public ProductDto updateProduct(ProductDto productDto) {
		Product product = productRepository.findByProductName(productDto.getName());
		ProductDto prodDto = new ProductDto();
		
		if (product != null) {
			
			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setPrice(productDto.getPrice());
			product.setCategory(productDto.getCategory());
			
			logger.info("updateProuct:{}", product);
			
			productRepository.updateProduct(product);
			product = productRepository.findByProductName(productDto.getName());
			

			prodDto.setCategory(product.getCategory());
			prodDto.setDescription(product.getDescription());
			prodDto.setName(product.getName());
			prodDto.setPrice(productDto.getPrice());
		}
		
		return prodDto;
	}
	
	/**
	 * 상품 수정 
	 * @param stockDto
	 */
	@Transactional
	public ProductDto saveProduct(ProductDto productDto) {
		Product product = productRepository.findByProductName(productDto.getName());
		ProductDto prodDto = new ProductDto();
		if (product == null) {
			product = new Product();
			
			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setPrice(productDto.getPrice());
			product.setCategory(productDto.getCategory());
			
			productRepository.saveProduct(product);
			
			product = productRepository.findByProductName(productDto.getName());
			// save stock 
			Stock stock = new Stock();
			stock.setProductId(product.getId());
			stock.setQty(stockQty);
			
			stockService.saveStock(stock);
			
			prodDto.setCategory(product.getCategory());
			prodDto.setDescription(product.getDescription());
			prodDto.setName(product.getName());
			prodDto.setPrice(product.getPrice());
			
		} else {
			throw new RuntimeException("상품이 존재합니다.");
		}
		
		return prodDto;

	}	
	
	
	
	
	/**
	 * 상품 삭제  
	 * @param productDto
	 */
	@Transactional
	public void deleteProduct(String productName) {
		Product product = productRepository.findByProductName(productName);
		logger.info("product id :{}, product: {}", productName, product);
		productRepository.deleteProduct(product.getId());
		
		stockService.deleteStockByProductId(product.getId());
	}		
}
