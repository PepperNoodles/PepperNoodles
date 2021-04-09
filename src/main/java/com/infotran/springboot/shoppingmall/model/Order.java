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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.infotran.springboot.commonmodel.UserAccount;



@Entity
@Table(name="Order")
@Component
public class Order {
	
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
	
	
}
