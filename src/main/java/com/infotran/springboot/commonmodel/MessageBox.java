package com.infotran.springboot.commonmodel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="messageBox")
@Component
public class MessageBox implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_message_id")
	private Integer userMessageId;
	
	
	@Column(name="text")
	private String text;
	
	@Column(name="time")
	private Date time;
	
	@Column(name="likeAmount")
	private Integer likeAmount;
	
	@Column(name="fk_netizen_account")
	@Transient
	private Integer fkNetizenAccount;
	
	@Column(name="fk_userAccount")
	@Transient
	private Integer fkUserAccount;
	
	@Column(name="fk_reply_user_messageBox_id")
	@Transient
	private Integer fk_reply_user_messageBox;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_netizen_account")
	private UserAccount netizenAccount;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_userAccount")
	private UserAccount userAccount;
	
	//replyMessageBox
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY/*, optional = false*/)
    @JoinColumn(name = "fk_reply_users_messageBox_id"/*,insertable = false,updatable = false*/)
	private MessageBox messageBox;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "messageBox",cascade = CascadeType.ALL)
	private List<MessageBox> replyMessageBoxes;

	
	
	public MessageBox() {
		super();
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



	public UserAccount getNetizenAccount() {
		return netizenAccount;
	}



	public void setNetizenAccount(UserAccount netizenAccount) {
		this.netizenAccount = netizenAccount;
	}



	public UserAccount getUserAccount() {
		return userAccount;
	}



	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}



	public MessageBox getMessageBox() {
		return messageBox;
	}



	public void setMessageBox(MessageBox messageBox) {
		this.messageBox = messageBox;
	}



	public List<MessageBox> getReplyMessageBoxes() {
		return replyMessageBoxes;
	}



	public void setReplyMessageBoxes(List<MessageBox> replyMessageBoxes) {
		this.replyMessageBoxes = replyMessageBoxes;
	}



	public Integer getUserMessageId() {
		return userMessageId;
	}



	public void setUserMessageId(Integer userMessageId) {
		this.userMessageId = userMessageId;
	}


	
	

}
