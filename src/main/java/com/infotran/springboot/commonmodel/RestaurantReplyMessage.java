package com.infotran.springboot.commonmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.crypto.Data;

import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "restaurantReplyMessage")
public class RestaurantReplyMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Restaurant_Reply_id")
	private Integer restaurantReplyId;
	
	@Transient
	@Column(name = "fk_ReplyNetizen_Account_id")
	private Integer replyNetizenAccountId;
	
	@Transient
	@Column(name = "fk_replyMessage_id")
	private Integer replyMessageId;

	@Column(name = "Reply_Text")
	private String replyText;

	@Column(name = "Time")
	private Data time;

	@Column(name = "LikeAmount")
	private Integer likeAmount;
	
	/** 一個會員可以有多個回覆留言 **/
	@ManyToOne
	@JoinColumn(name = "fk_ReplyNetizen_Account_id")
	private UserAccount userAccount;
	
	/** 被回覆的那則留言 **/
	@ManyToOne
	@JoinColumn(name = "fk_replyMessage_id")
	private RestaurantMessageBox restaurantMessageBox;

	public Integer getRestaurantReplyId() {
		return restaurantReplyId;
	}

	public void setRestaurantReplyId(Integer restaurantReplyId) {
		this.restaurantReplyId = restaurantReplyId;
	}

	public Integer getReplyNetizenAccountId() {
		return replyNetizenAccountId;
	}

	public void setReplyNetizenAccountId(Integer replyNetizenAccountId) {
		this.replyNetizenAccountId = replyNetizenAccountId;
	}

	public Integer getReplyMessageId() {
		return replyMessageId;
	}

	public void setReplyMessageId(Integer replyMessageId) {
		this.replyMessageId = replyMessageId;
	}

	public String getReplyText() {
		return replyText;
	}

	public void setReplyText(String replyText) {
		this.replyText = replyText;
	}

	public Data getTime() {
		return time;
	}

	public void setTime(Data time) {
		this.time = time;
	}

	public Integer getLikeAmount() {
		return likeAmount;
	}

	public void setLikeAmount(Integer likeAmount) {
		this.likeAmount = likeAmount;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public RestaurantMessageBox getRestaurantMessageBox() {
		return restaurantMessageBox;
	}

	public void setRestaurantMessageBox(RestaurantMessageBox restaurantMessageBox) {
		this.restaurantMessageBox = restaurantMessageBox;
	}
	
	
}
