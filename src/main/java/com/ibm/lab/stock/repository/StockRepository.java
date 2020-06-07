package com.ibm.lab.stock.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.lab.stock.model.ReservedStock;
import com.ibm.lab.stock.model.Stock;
import com.ibm.lab.stock.model.StockOrderHistory;

@Repository
public class StockRepository {
	private static final Logger logger = LoggerFactory.getLogger(StockRepository.class);
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * 등록 
	 * @param stockDto
	 * @return
	 */
	public int saveStock(Stock stock) {
		int record = sqlSessionTemplate.insert("stock.saveStock", stock);
		logger.info("stock rc:{}", record);
		
		return record;
	}
	
	/**
	 * stock 10 건 조회 
	 * @return
	 */
	public List<Stock> findAll() {
		List<Stock> stocks = sqlSessionTemplate.selectList("stock.findAll", 10L);
		logger.info("stock list:{}", stocks);
		
		return stocks;
	}
	
	public Stock findByProductId(Long id) {
		Stock stock = sqlSessionTemplate.selectOne("stock.findByProductId", id);
		
		return stock;
	}
	
	public Long getSeqReservedStock() {
		Long seq = sqlSessionTemplate.selectOne("stock.getSeqReservedStock");
		
		return seq;
	}
	
	public Long genSeqReservedPayment() {
		Long seq = sqlSessionTemplate.selectOne("stock.genSeqReservedStock");
		
		return seq;
	}
	
	public Stock findByProductName(String productName) {
		Stock stock = sqlSessionTemplate.selectOne("stock.findByProductName", productName);
		
		return stock;
	}
	
	public ReservedStock findByStockId(Long id) {
		ReservedStock product = sqlSessionTemplate.selectOne("stock.findByStockId", id);
		
		return product;
	}
	
	public int saveReservedStock(ReservedStock reservedStock) {
		int record = sqlSessionTemplate.insert("stock.saveReservedStock", reservedStock);
		
		return record;
	}
	
	public StockOrderHistory getStockOrderHistory(String productName) {
		StockOrderHistory stockOrderHistory = sqlSessionTemplate.selectOne("stock.getStockOrderHistory", productName);
		
		return stockOrderHistory;
	}
		
	public int deleteStock(Long id) {
		int record = sqlSessionTemplate.delete("stock.deleteStock", id);
		
		return record;
	}	
	
	public int deleteStockByProductId(Long id) {
		logger.info("productId:{}", id);
		int record = sqlSessionTemplate.delete("stock.deleteStockByProductId", id);
		
		return record;
	}
	
	public int saveStockOrderHistory(StockOrderHistory stockOrderHistory) {
		int record = sqlSessionTemplate.insert("stock.saveStockOrderHistory", stockOrderHistory);
		
		return record;
	}
	
	
	
	public ReservedStock getReservedStock(String orderId) {
		ReservedStock reservedStock = sqlSessionTemplate.selectOne("stock.getReservedStock", orderId);
		logger.info("reservedStock rc:{}", reservedStock);
		
		return reservedStock;
	}
	
	public ReservedStock findByReservedStockId(Long id) {
		ReservedStock reservedStock = sqlSessionTemplate.selectOne("stock.findByReservedStockId", id);
		logger.info("reservedStock rc:{}", reservedStock);
		
		return reservedStock;
	}
	
	/**
	 * 등록 
	 * @param stockDto
	 * @return
	 */
	public Stock findByOrderId(String orderId) {
		Stock stock = sqlSessionTemplate.selectOne("stock.findByOrderId", orderId);
		logger.info("stock :{}", stock);
		
		return stock;
	}
}
