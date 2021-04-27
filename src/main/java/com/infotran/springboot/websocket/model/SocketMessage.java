package com.infotran.springboot.websocket.model;



import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infotran.springboot.commonmodel.UserAccount;

@Entity
@Table(name="socketMessage")
@Component
public class SocketMessage {
	
	@Id
	@Column(name="socketMessage_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer socketMessageId;
	
	@Transient
	@Column(name="fk_UserAccount")
	private Integer fkUserAccount;
	
	//從Message list找往左找
	@ManyToOne
	@JoinColumn(name="fk_UserAccount")
	private UserAccount mainUser; 
	
	@Transient
	@Column(name="fk_toUserAccount")
	private Integer fkToUserAccount;
	
	//從Message list找往右邊找
	@ManyToOne
	@JoinColumn(name="fk_toUserAccount")
	private UserAccount toUserAccounts;
	
	@Column(name="message")
	private String message;
	
	@Column(name="date")
	private Date date;

	public Integer getSocketMessageId() {
		return socketMessageId;
	}

	public void setSocketMessageId(Integer socketMessageId) {
		this.socketMessageId = socketMessageId;
	}

	public Integer getFkUserAccount() {
		return fkUserAccount;
	}

	public void setFkUserAccount(Integer fkUserAccount) {
		this.fkUserAccount = fkUserAccount;
	}

	public UserAccount getMainUser() {
		return mainUser;
	}

	public void setMainUser(UserAccount mainUser) {
		this.mainUser = mainUser;
	}

	public Integer getFkToUserAccount() {
		return fkToUserAccount;
	}

	public void setFkToUserAccount(Integer fkToUserAccount) {
		this.fkToUserAccount = fkToUserAccount;
	}



	public UserAccount getToUserAccounts() {
		return toUserAccounts;
	}

	public void setToUserAccounts(UserAccount toUserAccounts) {
		this.toUserAccounts = toUserAccounts;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
