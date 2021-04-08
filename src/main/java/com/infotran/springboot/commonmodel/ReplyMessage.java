package com.infotran.springboot.commonmodel;

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
@Table(name="replyMessageBox")
public class ReplyMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_Reply_id")
	private Integer userReplyId;
	
	@Column(name="fk_ReplyNetizen_Account_id ")
	@Transient
	private Integer fkReplyNetizenAccountId;
	
	@Column(name="fk_UserMessage_id ")
	@Transient
	private Integer fkUserMessageId;
	
	@Column(name="Reply_Text ")
	private String replyText ;
	
	@Column(name="time")
	private Date time;
	
	@Column(name="likeAmount")
	private Integer likeAmount;
	
	@Column(name="fk_netizen_account_id")
	@Transient
	private Integer fkNetizenAccountId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_ReplyNetizen_Account_id")
	private UserAccount replyuserAccount;
	
	//CascadeType要改嗎??
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_UserMessage_id")
	private MessageBox userMessageBox;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_netizen_account_id")
	private UserAccount commentUserAccount;


	public ReplyMessage() {
		super();
	}
	
	public Integer getUserReplyId() {
		return userReplyId;
	}




	public void setUserReplyId(Integer userReplyId) {
		this.userReplyId = userReplyId;
	}




	public Integer getFkReplyNetizenAccountId() {
		return fkReplyNetizenAccountId;
	}




	public void setFkReplyNetizenAccountId(Integer fkReplyNetizenAccountId) {
		this.fkReplyNetizenAccountId = fkReplyNetizenAccountId;
	}




	public Integer getFkUserMessageId() {
		return fkUserMessageId;
	}




	public void setFkUserMessageId(Integer fkUserMessageId) {
		this.fkUserMessageId = fkUserMessageId;
	}




	public String getReplyText() {
		return replyText;
	}




	public void setReplyText(String replyText) {
		this.replyText = replyText;
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




	public Integer getFkNetizenAccountId() {
		return fkNetizenAccountId;
	}




	public void setFkNetizenAccountId(Integer fkNetizenAccountId) {
		this.fkNetizenAccountId = fkNetizenAccountId;
	}









	
	

	
	
	

}
