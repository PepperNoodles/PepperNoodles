package com.infotran.springboot.common.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "forum")
@Component
public class Forum {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "forum_id")
	private Integer forumId;
	
	@Column(name = "content")
	private String content;

	@Column(name = "fileContent")
	private Blob fileContent;
	
	@Column(name = "likeAmount")
	private Integer likeAmount;

	@Column(name = "forumTime")
	@Transient
	private Date forumTime;
	
	@Column(name = "fk_useraccount_id")
	@Transient
	private Integer userAccountId;

	@Column(name = "forumPictureName")
	private String forumPictureName;
	
	@Column(name = "fk_restaurant_id")
	@Transient
	private Integer restaurantId;

	// =============================================================
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_useraccount_id")
	private UserAccount userAccount;

	// =============================================================
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_restaurant_id")
	private Restaurant restaurant;
	
	//forumCollections =============================================================
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "forumCollections", joinColumns = {
			@JoinColumn(name = "fk_Forum_id", referencedColumnName = "forum_id") }, inverseJoinColumns = {
					@JoinColumn(name = "fk_userAccount_id", referencedColumnName = "account_id") })
	private Set<UserAccount> colloctors = new HashSet<UserAccount>();
	
	// =============================================================

	@ManyToMany(mappedBy = "forums")
	private Set<FoodTag> forumTags = new HashSet<FoodTag>();
		
	// ForumMessageBox=============================================================
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "forum",cascade = CascadeType.ALL)
	private List<ForumMessageBox> forumMsnBox = new ArrayList<ForumMessageBox>();
	
	// =============================================================

	public Integer getForumId() {
		return forumId;
	}

	public void setForumId(Integer forumId) {
		this.forumId = forumId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Blob getFileContent() {
		return fileContent;
	}

	public void setFileContent(Blob fileContent) {
		this.fileContent = fileContent;
	}

	public Integer getLikeAmount() {
		return likeAmount;
	}

	public void setLikeAmount(Integer likeAmount) {
		this.likeAmount = likeAmount;
	}

	public Date getForumTime() {
		return forumTime;
	}

	public void setForumTime(Date forumTime) {
		this.forumTime = forumTime;
	}

	public Integer getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Integer userAccountId) {
		this.userAccountId = userAccountId;
	}

	public String getForumPictureName() {
		return forumPictureName;
	}

	public void setForumPictureName(String forumPictureName) {
		this.forumPictureName = forumPictureName;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}


}
