package com.ibm.lab.stock.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.ibm.lab.stock.dto.StockDto;
import com.ibm.lab.stock.service.StockService;

@Component
public class StockChannelAdapter {
	private static final Logger logger = LoggerFactory.getLogger(StockChannelAdapter.class);
	
	@Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;
	
	@Value("${kafka.stock.topic}")
	private String TOPIC;
	
	private StockService stockService;
	
	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}
	
    public void publish(final String message) {
        this.kafkaTemplate.send(TOPIC, message);
    }
    
    @KafkaListener(topics = "${kafka.stock.topic}")
    public void subscribe(final String message, Acknowledgment ack) {
        logger.info(String.format("Message Received : [%s]", message));
        try {
            StockDto stockDto = StockDto.deserializeJSON(message);

            logger.info("StockDto  : {}", stockDto);
            // 이미 처리된 주문인지 확인
            if(stockService.isAlreadyProcessedOrderId(stockDto.getOrderId())) {
                logger.info(String.format("AlreadyProcessedOrderId : [%s]", stockDto.getOrderId()));
            } else {
                // 미 처리 주문일 경우 재고 처리
                if(stockDto.getAdjustmentType().equals("REDUCE")) {
                    stockService.decreaseStock(stockDto);
                }
            }
            // Kafka Offset Manual Commit
            ack.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
