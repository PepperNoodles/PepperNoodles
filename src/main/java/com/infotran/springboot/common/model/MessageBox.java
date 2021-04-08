package com.infotran.springboot.common.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="messageBox")
public class MessageBox {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_message_id")
	private Integer userMessageId;
	
	@Column(name="fk_netizen_account")
	@Transient
	private Integer fkNetizenAccount;
	
	@Column(name="text")
	private String text;
	
	@Column(name="time")
	private Date time;
	
	@Column(name="likeAmount")
	private Integer likeAmount;
	
	@Column(name="fk_userAccount")
	@Transient
	private Integer fkUserAccount;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_netizen_account")
	private UserAccount netizenAccount;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_userAccount")
	private UserAccount UserAccount;
	
	//replyMessageBox
	@OneToOne(mappedBy ="userMessageBox",cascade = CascadeType.ALL)
	private ReplyMessage replyMessage;

	
	
	public MessageBox() {
		super();
	}

	public Integer getUserMessageId() {
		return userMessageId;
	}

	public void setUserMessageId(Integer userMessageId) {
		this.userMessageId = userMessageId;
	}

	public Integer getFkNetizenAccount() {
		return fkNetizenAccount;
	}

	public void setFkNetizenAccount(Integer fkNetizenAccount) {
		this.fkNetizenAccount = fkNetizenAccount;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getLikeAmount() {
		return likeAmount;
	}

	public void setLikeAmount(Integer likeAmount) {
		this.likeAmount = likeAmount;
	}

	public Integer getFkUserAccount() {
		return fkUserAccount;
	}

	public void setFkUserAccount(Integer fkUserAccount) {
		this.fkUserAccount = fkUserAccount;
	}


	public UserAccount getUserAccount() {
		return UserAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		UserAccount = userAccount;
	}
	
	
	

}
