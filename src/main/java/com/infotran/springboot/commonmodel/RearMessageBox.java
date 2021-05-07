package com.infotran.springboot.commonmodel;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "rearMessageBox")
@Component
public class RearMessageBox {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rearMessage_id")
	private Integer rearMessageId;
	
//	@Column(name = "acoount_index")
//	private String accountIndex;
	
	@Column(name = "messageText")
	private String messageText;
		
	@Column(name = "time")
	private Date time;
	
	@Column(name = "condition")
	private String condition;
	
	@Column(name = "fk_userAccount_id", insertable=false, updatable=false)
	private Integer userAccountId;

	/** 1個User可以有多個訊息 **/
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_userAccount_id")
	private UserAccount userAccount;
	
		
	public RearMessageBox() {
		super();
	}


	public Integer getRearMessageId() {
		return rearMessageId;
	}


	public void setRearMessageId(Integer rearMessageId) {
		this.rearMessageId = rearMessageId;
	}


	public String getMessageText() {
		return messageText;
	}


	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}


	public String getCondition() {
		return condition;
	}
	
	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	public void setCondition(String condition) {
		this.condition = condition;
	}


	public Integer getUserAccountId() {
		return userAccountId;
	}


	public void setUserAccountId(Integer userAccountId) {
		this.userAccountId = userAccountId;
	}


	public UserAccount getUserAccount() {
		return userAccount;
	}


	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

}
