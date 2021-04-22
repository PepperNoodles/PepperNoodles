package com.infotran.springboot.shoppingmall.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="OrderDetail")
@Component
public class OrderDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="OrderDetail_id")
	private Integer OrderDetailId;
	
	@Column(name="fk_Order_id")
	@Transient
	private Integer fkOrderId;
	
	@Column(name="fk_Product_id")
	@Transient
	private Integer fkProductId;
	

	private Integer Amount;

	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_Order_id")
	@JsonIgnore
    private OrderList orderlist;
	

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_Product_id")
	@JsonIgnore
	private  Product product;
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
	
	
}
