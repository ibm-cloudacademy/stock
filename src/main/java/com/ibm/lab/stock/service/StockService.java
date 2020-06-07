package com.ibm.lab.stock.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.lab.product.model.Product;
import com.ibm.lab.product.service.ProductService;
import com.ibm.lab.stock.adapter.StockChannelAdapter;
import com.ibm.lab.stock.dto.StockDto;
import com.ibm.lab.stock.dto.StockOrderHistoryDto;
import com.ibm.lab.stock.dto.StockProductDto;
import com.ibm.lab.stock.model.ReservedStock;
import com.ibm.lab.stock.model.Status;
import com.ibm.lab.stock.model.Stock;
import com.ibm.lab.stock.model.StockOrderHistory;
import com.ibm.lab.stock.repository.StockRepository;

@Service
public class StockService {
	private static final Logger logger = LoggerFactory.getLogger(StockService.class);
	
	@Autowired
	private StockChannelAdapter stockChannelAdapter;
	
	@Autowired
	private StockRepository stockRepository;
	
	@Autowired
	private ProductService productService;
	/**
	 * 재고량 조회 
	 * @return
	 */
	public List<Stock> findAll() {
		return stockRepository.findAll();
	}
	
	/**
	 * 재고량 수정 
	 * @param stockDto
	 */
	public void saveStock(Stock stock) {
		
		logger.info("stockService : {}", stock);
		stockRepository.saveStock(stock);
	}
	
	public int deleteStock(Long id) {
		return stockRepository.deleteStock(id);
	}
	
	public int deleteStockByProductId(Long id) {
		return stockRepository.deleteStockByProductId(id);
	}
	
	public void saveStockProduct(StockProductDto stockProductDto) {
		Product product = productService.findById(stockProductDto.getProductId());
		Stock stock = new Stock();
		stock.setProductId(product.getId());
		stock.setQty(stockProductDto.getQty());
		
		logger.info("stockService : {}", stock);
		stockRepository.saveStock(stock);
	}
	
    public ReservedStock reservedStock(final StockDto stockDto) {
        ReservedStock reservedStock = new ReservedStock(stockDto);

        Long reservedStockId = stockRepository.genSeqReservedPayment();
        reservedStock.setId(reservedStockId);
        logger.info("Reserved Stock :{}" ,reservedStock);
        
        stockRepository.saveReservedStock(reservedStock);

        
        return reservedStock;
    }
    
    @Transactional
    public void confirmStock(Long id) {
        ReservedStock reservedStock = stockRepository.findByReservedStockId(id);

        if(reservedStock == null) {
            throw new IllegalArgumentException("Not found");
        }

        reservedStock.validate();

        // ReservedStock 상태를 Confirm 으로 변경
        reservedStock.setStatus(Status.CONFIRMED.toString());
        stockRepository.saveReservedStock(reservedStock);

        logger.info("publish message:{}", reservedStock.getResources());
        // Messaging Queue 로 전송
        stockChannelAdapter.publish(reservedStock.getResources());
        logger.info("Confirm Stock :" + id);
    }


    public StockOrderHistoryDto getStockOrderHistory(String orderId) {
    	StockOrderHistory stockOrderHistory = stockRepository.getStockOrderHistory(orderId);
    	
    	logger.info("get stockOrderHistory : {}",stockOrderHistory);
    	
    	StockOrderHistoryDto stockOrderHistoryDto = null;
    	
    	if (stockOrderHistory != null) {
    		
    		Product product = productService.findById(stockOrderHistory.getProductId());
    		
        	stockOrderHistoryDto = new StockOrderHistoryDto();
        	stockOrderHistoryDto.setProductName(product.getName());
        	stockOrderHistoryDto.setOrderId(stockOrderHistory.getOrderId());
        	stockOrderHistoryDto.setAdjustmentType(stockOrderHistory.getAdjustmentType());
        	stockOrderHistoryDto.setQty(stockOrderHistory.getQty());
    	}
       
        return stockOrderHistoryDto;
    }

    @Transactional
    public void decreaseStock(StockDto stockDto) {
       logger.info("decreaseStock stockDto:{}", stockDto);
	   Stock stock = stockRepository.findByProductName(stockDto.getProductCode());
	   stock.decrease(stockDto.getQty());
	   
	   logger.info("decreaseStock stock :{}", stock);
	   // update stock
	   stockRepository.saveStock(stock);
	   
	   // save stock_order_history
	   StockOrderHistory stockOrderHistory = new StockOrderHistory(stock.getProductId(), stockDto.getOrderId(), stockDto.getAdjustmentType(), stockDto.getQty());
	   stockRepository.saveStockOrderHistory(stockOrderHistory);
	   
       logger.info(String.format("Stock decreased [orderId: %s][productId : %s][qty  : %d]",
    		   stockDto.getOrderId(), stockDto.getProductCode(), stockDto.getQty()));
    }
    
    @Transactional
    public void cancelStock(Long id) {
    	logger.info("cancelStock:{}", id);
    	ReservedStock reservedStock = stockRepository.findByReservedStockId(id);
    	
        reservedStock.setStatus(Status.CANCEL.toString());
        stockRepository.saveReservedStock(reservedStock);

        logger.info("Cancel Stock :" + id);
    }

    public boolean isAlreadyProcessedOrderId(String orderId) {
        Stock stock = stockRepository.findByOrderId(orderId);

        if(stock == null) {
            return false;
        } else {
            return true;
        }
    }
}
