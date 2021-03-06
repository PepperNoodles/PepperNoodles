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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	private String messageText;//kity
	
	//發布訊息時間
	@Column(name = "time")
	private Date time;
	
	//更新訊息時間
	@Column(name = "updateTime")
	@JsonInclude(content = Include.NON_NULL)
	private Date updateTime;	

	@Column(name = "condition")
	private boolean  condition;
	

//	@Transient
	@Column(name = "fk_userAccount_id",insertable=false,updatable=false)
	private Integer userAccountId;//transient對應不到資料表 //// ↑上面是指可以查詢 但是不能新增跟修改
	

	/** 1個User可以有多個訊息 **/
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_userAccount_id")
//	@JsonIgnore 
	private UserAccount userAccount;//jsonignore不回傳出去在json檔
	
		
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


	public boolean isCondition() {
		return condition;
	}
	
	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public void setCondition(boolean condition) {
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
