package com.infotran.springboot.shoppingmall.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infotran.springboot.commonmodel.UserAccount;


@Entity
@Table(name="OrderList")
@Component
public class OrderList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Order_id")
	private Integer orderId;
	
	@Column(name="OrderCreatedDate")
	private Date orderCreatedDate;
	
	/* 多對一 */
	@Column(name="fk_userAccount_id")
	@Transient
	private Integer fkUserAccountId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_userAccount_id")
	private UserAccount user;
	
	@Column(name="ReceiveAddress")
	private String receiveAddress;
	
	@Column(name="PaidDate")
	private Date paidDate;
	
	@Column(name="Status")
	private String status;
	
	@Column(name="TotalCost")
	private Integer totalCost;
	
	@OneToMany(mappedBy = "orderlist",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<OrderDetail> odetails = new HashSet<OrderDetail>();
	
	//
	@PrePersist
	protected void createOrderDate() {
		orderCreatedDate = new Date();
	}
	
	@PreUpdate
	protected void updateOrderDate() {
		orderCreatedDate = new Date();
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getOrderCreatedDate() {
		return orderCreatedDate;
	}

	public void setOrderCreatedDate(Date orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
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
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
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

	public Set<OrderDetail> getOdetails() {
		return odetails;
	}

	public void setOdetails(Set<OrderDetail> odetails) {
		this.odetails = odetails;
	}

	//
	
	
}
