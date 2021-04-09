package com.infotran.springboot.shoppingmall.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.infotran.springboot.common.model.FoodTag;
import com.infotran.springboot.loginsystem.model.UserAccount;
import com.infotran.springboot.loginsystem.model.UserDetail;

@Entity
@Table(name="OrderList")
@Component
public class OrderList {
	
	@Id
	@Column(name="Order_id")
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
	
	@ManyToMany(mappedBy = "order")
	private Set<Product> products = new HashSet<Product>();

	public Integer getOrderId() {
		return OrderId;
	}

	public void setOrderId(Integer orderId) {
		OrderId = orderId;
	}

	public Date getOrderCreatedDate() {
		return OrderCreatedDate;
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

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	
}
