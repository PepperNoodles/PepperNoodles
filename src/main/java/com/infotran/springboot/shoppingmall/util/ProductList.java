package com.infotran.springboot.shoppingmall.util;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ProductList {

	private Integer productId;
	
	private String productName;
	
	private Integer productPrice;
	
	private Integer quantity;
	
	private String productDetailClassName;
	
	private Date realeasedDate;
	
	private Integer salesamount;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ProductList() {
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getProductDetailClassName() {
		return productDetailClassName;
	}

	public void setProductDetailClassName(String productDetailClassName) {
		this.productDetailClassName = productDetailClassName;
	}

	public Date getRealeasedDate() {
		return realeasedDate;
	}

	public void setRealeasedDate(Date realeasedDate) {
		this.realeasedDate = realeasedDate;
	}

	public Integer getSalesamount() {
		return salesamount;
	}

	public void setSalesamount(Integer salesamount) {
		this.salesamount = salesamount;
	}

	@Override
	public String toString() {
		return "ProductList [productId=" + productId + ", productName=" + productName + ", productPrice=" + productPrice
				+ ", quantity=" + quantity + ", productDetailClassName=" + productDetailClassName + ", realeasedDate="
				+ realeasedDate + ", salesamount=" + salesamount + "]";
	}
	
}
