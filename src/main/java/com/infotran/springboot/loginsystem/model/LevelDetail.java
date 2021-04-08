package com.infotran.springboot.loginsystem.model;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name="levelDetail")
@Component
public class LevelDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LevelDetail_id")
	private Integer levelDetailId;
	
	@Column(name = "level")
	private String level;
	
	@Column(name = "postAmount")
	private Integer postAmount;
	
	@Column(name = "likesAmount")
	private Integer likesAmount;
	
	@Column(name = "followers")
	private Integer followers;
	
	@Column(name = "replyAmount")
	private Integer replyAmount;
	
	@Column(name = "logInAmount")
	private Integer logInAmount;
	
	@Column(name = "purchaseAmount")
	private Integer purchaseAmount;
	
	@OneToOne(mappedBy = "levelDetail")
	private UserAccount userAccount;
	
	public LevelDetail() {
	}

	public Integer getLevelDetailId() {
		return levelDetailId;
	}

	public void setLevelDetailId(Integer levelDetailId) {
		this.levelDetailId = levelDetailId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getPostAmount() {
		return postAmount;
	}

	public void setPostAmount(Integer postAmount) {
		this.postAmount = postAmount;
	}

	public Integer getLikesAmount() {
		return likesAmount;
	}

	public void setLikesAmount(Integer likesAmount) {
		this.likesAmount = likesAmount;
	}

	public Integer getFollowers() {
		return followers;
	}

	public void setFollowers(Integer followers) {
		this.followers = followers;
	}

	public Integer getReplyAmount() {
		return replyAmount;
	}

	public void setReplyAmount(Integer replyAmount) {
		this.replyAmount = replyAmount;
	}

	public Integer getLogInAmount() {
		return logInAmount;
	}

	public void setLogInAmount(Integer logInAmount) {
		this.logInAmount = logInAmount;
	}

	public Integer getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(Integer purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}


	

}
