package com.ibm.lab.stock.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.lab.stock.dto.StockDto;

public class ReservedStock {
    // 3초 타임 아웃
    private static final long TIMEOUT = 3L;

    private Long id;

    private String resources;

    private String status;

    private LocalDateTime created;

    private LocalDateTime expires;

    public ReservedStock() {
    }

    public ReservedStock(StockDto stockDto) {
        try {
            this.resources = stockDto.serializeJSON();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        this.created = LocalDateTime.now();
        this.expires = created.plus(TIMEOUT, ChronoUnit.SECONDS);
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }

    public String getResources() {
        return this.resources;
    }
  
    
    
    public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public void setExpires(LocalDateTime expires) {
		this.expires = expires;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void validate() {
        validateStatus();
        validateExpired();
    }

    public void validateStatus() {
        if(this.getStatus() == Status.CANCEL.toString() || this.getStatus() == Status.CONFIRMED.toString()) {
            throw new IllegalArgumentException("Invalidate Status");
        }
    }

    private void validateExpired() {
        if(LocalDateTime.now().isAfter(this.expires)) {
            throw new IllegalArgumentException("Expired");
        }
    }
    
    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", resources='" + resources + '\'' +
                ", created='" + created + '\'' +
                ", expires=" + expires +
                '}';
    }
}
