package com.infotran.springboot.shoppingmall.model;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class OrderListView {
	
	
	private Integer uuid;
	
	private String productName;
	
	private Date orderCreateDate;
	
	private String status;
	
	private Integer totalCost;
	
	
	public OrderListView() {
	}


	public Integer getUuid() {
		return uuid;
	}


	public void setUuid(Integer uuid) {
		this.uuid = uuid;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public Date getOrderCreateDate() {
		return orderCreateDate;
	}


	public void setOrderCreateDate(Date orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Integer getTotalCost() {
		return totalCost;
	}


	public void setTotalCost(Integer totalCost) {
		this.totalCost = totalCost;
	}
	
	
	
}
