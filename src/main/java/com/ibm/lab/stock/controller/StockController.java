package com.ibm.lab.stock.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.lab.stock.adapter.ParticipantLink;
import com.ibm.lab.stock.dto.StockDto;
import com.ibm.lab.stock.dto.StockOrderHistoryDto;
import com.ibm.lab.stock.dto.StockProductDto;
import com.ibm.lab.stock.model.ReservedStock;
import com.ibm.lab.stock.model.Stock;
import com.ibm.lab.stock.service.StockService;

@RestController
public class StockController {
	private static final Logger logger = LoggerFactory.getLogger(StockService.class);
	
	private StockService stockService;

    @Autowired
    public void setProductService(StockService stockService) {
        this.stockService = stockService;
    }
    
    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getStocks() {
    	List products = stockService.findAll();
    	return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @GetMapping("/stocks/{orderId}/product")
    public ResponseEntity<StockOrderHistoryDto> getStockOrderHistory(@PathVariable String orderId) {
    	StockOrderHistoryDto stockOrderHistory = stockService.getStockOrderHistory(orderId);
    	 
    	return new ResponseEntity<>(stockOrderHistory, HttpStatus.OK);
    }
    
    @PostMapping("/stocks/{productId}/product")
    public ResponseEntity<Stock> saveStock(@RequestBody StockProductDto stockProductDto) {
    	logger.info("1. stock StockProductDto : {}", stockProductDto);
    	stockService.saveStockProduct(stockProductDto);
    	return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @PostMapping("/stocks")
    public ResponseEntity<ParticipantLink> tryStockAdjustment(@RequestBody StockDto stockDto) {
    	logger.info("1. tryStockAdjustment reservedStock : {}", stockDto);
        final ReservedStock reservedStock = stockService.reservedStock(stockDto);

        logger.info("2. reservedStock : {}", reservedStock);
        final ParticipantLink participantLink = buildParticipantLink(reservedStock.getId(), reservedStock.getExpires());

        return new ResponseEntity<>(participantLink, HttpStatus.CREATED);
    }
    
    private ParticipantLink buildParticipantLink(final Long id, final LocalDateTime expired) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

        return new ParticipantLink(location, expired);
    }
    
    @PutMapping("/stocks/{id}")
    public ResponseEntity<Void> confirmStockAdjustment(@PathVariable Long id) {
        try {
            stockService.confirmStock(id);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> cancelStockAdjustment(@PathVariable Long id) {
        stockService.cancelStock(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
