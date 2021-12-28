package com.currency.converter.app.dto;

import java.math.BigDecimal;

public class ItemDTO {

    private String item;
    private BigDecimal price;
    private String currency;
    
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Override
	public String toString() {
		return "[item= " + item + ", price= " + price + ", currency= " + currency + "]";
	}

}
