package com.infotran.springboot.shoppingmall.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="OrderDetail")
@Component
public class OrderDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="OrderDetail_id")
	private Integer OrderDetailId;
	
	@Column(name="fk_Order_id")
	private Integer fkOrderId;
	
	@Column(name="fk_Product_id")
	private Integer fkProductId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_Order_id",insertable = false,updatable = false)
	@JsonIgnore
    private OrderList orderlist;
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "fk_Product_id",insertable = false,updatable = false)
	@JsonIgnore
	private  Product product;
	
	private Integer Amount;
	//
	public OrderDetail() {
	}
	
	public Integer getOrderDetailId() {
		return OrderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		OrderDetailId = orderDetailId;
	}

	public Integer getFkOrderId() {
		return fkOrderId;
	}

	public void setFkOrderId(Integer fkOrderId) {
		this.fkOrderId = fkOrderId;
	}

	public Integer getFkProductId() {
		return fkProductId;
	}

	public void setFkProductId(Integer fkProductId) {
		this.fkProductId = fkProductId;
	}

	public OrderList getOrderlist() {
		return orderlist;
	}

	public void setOrderlist(OrderList orderlist) {
		this.orderlist = orderlist;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getAmount() {
		return Amount;
	}

	public void setAmount(Integer amount) {
		Amount = amount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Amount == null) ? 0 : Amount.hashCode());
		result = prime * result + ((OrderDetailId == null) ? 0 : OrderDetailId.hashCode());
		result = prime * result + ((fkOrderId == null) ? 0 : fkOrderId.hashCode());
		result = prime * result + ((fkProductId == null) ? 0 : fkProductId.hashCode());
		result = prime * result + ((orderlist == null) ? 0 : orderlist.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDetail other = (OrderDetail) obj;
		if (Amount == null) {
			if (other.Amount != null)
				return false;
		} else if (!Amount.equals(other.Amount))
			return false;
		if (OrderDetailId == null) {
			if (other.OrderDetailId != null)
				return false;
		} else if (!OrderDetailId.equals(other.OrderDetailId))
			return false;
		if (fkOrderId == null) {
			if (other.fkOrderId != null)
				return false;
		} else if (!fkOrderId.equals(other.fkOrderId))
			return false;
		if (fkProductId == null) {
			if (other.fkProductId != null)
				return false;
		} else if (!fkProductId.equals(other.fkProductId))
			return false;
		if (orderlist == null) {
			if (other.orderlist != null)
				return false;
		} else if (!orderlist.equals(other.orderlist))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
	
	
	
}
