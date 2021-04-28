package com.infotran.springboot.shoppingmall.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.infotran.springboot.commonmodel.UserAccount;


@Entity
@Table(name="OrderList")
@Component
public class OrderList implements Delayed{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Order_id")
	private Integer orderId;
	
	@Column(name="UUID")
	private Long uuid;
	
	@Column(name="OrderCreatedDate")
	private Date orderCreatedDate;
	
	/* 多對一 */
	@Column(name="fk_userAccount_id")
	@Transient
	private Integer fkUserAccountId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_userAccount_id")
	@JsonIgnore
	private UserAccount user;
	
	@Column(name="ReceiveName")
	private String receiveName;
	
	@Column(name="ReceivePhone")
	private String receivePhone;
	
	@Column(name="ReceiveAddress")
	private String receiveAddress;
	
	@Column(name="PaidDate")
	@JsonInclude(content = Include.NON_NULL)
	private Date paidDate;
	
	@Column(name="Status")
	@JsonInclude(content = Include.NON_NULL)
	private String status;
	
	@Column(name="TotalCost")
	private Integer totalCost;
	
	@OneToMany(mappedBy = "orderlist",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<OrderDetail> odetails = new HashSet<OrderDetail>();
	
	@Transient
	private Date cancelTime;
	
	
	//
	@PrePersist
	protected void createOrderDate() {
		orderCreatedDate = new Date();
	}
	
	public OrderList() {
		
	}

	public OrderList(Integer orderId, Long uuid, Date orderCreatedDate, Integer fkUserAccountId, UserAccount user,
			String receiveName, String receivePhone, String receiveAddress, Date paidDate, String status,
			Integer totalCost, Set<OrderDetail> odetails, Date cancelTime) {
		this.orderId = orderId;
		this.uuid = uuid;
		this.orderCreatedDate = orderCreatedDate;
		this.fkUserAccountId = fkUserAccountId;
		this.user = user;
		this.receiveName = receiveName;
		this.receivePhone = receivePhone;
		this.receiveAddress = receiveAddress;
		this.paidDate = paidDate;
		this.status = status;
		this.totalCost = totalCost;
		this.odetails = odetails;
		this.cancelTime = cancelTime;
	}





	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
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

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	@Override
	public int compareTo(Delayed o) {
		return this.getCancelTime().compareTo(((OrderList) o).getCancelTime());
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(cancelTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	
	
}
