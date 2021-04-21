package com.infotran.springboot.shoppingmall.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.infotran.springboot.commonmodel.UserAccount;


@Entity
@Table(name="OrderList")
@Component
public class OrderList {
	
	@Id
	@Column(name="Order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer OrderId;
	
	private Date OrderCreatedDate;
	
	/* 多對一 */
	@Column(name="fk_userAccount_id")
	@Transient
	private Integer fkUserAccountId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_userAccount_id")
	private UserAccount user;
	
	private String ReceiveAddress;
	
	private Date PaidDate;
	
	private String Status;
	
	@OneToMany(mappedBy = "orderlist",cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<OrderDetail> products = new HashSet<OrderDetail>();
	
	
	private Integer TotalCost;
	
//	@PrePersist
//	protected void onCreate() {
//		setOrderId(java.util.UUID.randomUUID());
//	}
	
	//

	
	
	public Date getOrderCreatedDate() {
		return OrderCreatedDate;
	}

	public Integer getOrderId() {
		return OrderId;
	}

	public void setOrderId(Integer orderId) {
		OrderId = orderId;
	}

	public void setOrderCreatedDate(Date orderCreatedDate) {
		OrderCreatedDate = orderCreatedDate;
	}

	public Integer getFkUserAccountId() {
		return fkUserAccountId;
	}

	public void setFkUserAccountId(Integer fkUserAccountId) {
		this.fkUserAccountId = fkUserAccountId;
	}

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public String getReceiveAddress() {
		return ReceiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		ReceiveAddress = receiveAddress;
	}

	public Date getPaidDate() {
		return PaidDate;
	}

	public void setPaidDate(Date paidDate) {
		PaidDate = paidDate;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public Set<OrderDetail> getProducts() {
		return products;
	}

	public void setProducts(Set<OrderDetail> products) {
		this.products = products;
	}

	public Integer getTotalCost() {
		return TotalCost;
	}

	public void setTotalCost(Integer totalCost) {
		TotalCost = totalCost;
	}
	
	
}
