package com.infotran.springboot.commonmodel;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "userAccount",uniqueConstraints = { @UniqueConstraint(columnNames = "acoount_index") })
@Component
public class UserAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Integer accountId;

	@Column(name = "acoount_index")
	private String accountIndex;

	@Column(name = "password")
	private String password;
	
	@Column(name = "accountType")
	@Transient
	private Integer accountType;

	@Column(name = "fk_accountDetail_id")
	@Transient
	private Integer accountDetailId;
	
	@Column(name = "fk_companyDetail_id")
	@Transient
	private Integer companyDetailId;

	@Column(name = "fk_levelDetail_id")
	@Transient
	private Integer levelDetailId;

	// =============================================================
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_accountDetail_id")
	private UserDetail userAccountDetail;

	// =============================================================
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_companyDetail_id")
	private CompanyDetail companyDetail;

	// =============================================================
	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_levelDetail_id")
	private CompanyDetail levelDetail;

	// foodtags=============================================================
	
	@ManyToMany(mappedBy = "users")
	private Set<FoodTag> userTags = new HashSet<FoodTag>();
	
	// RestaurantFollowerForm=============================================================
	
//	@ManyToMany(mappedBy = "userAccountIDs")
//	private Set<Restaurant> restaurants = new HashSet<Restaurant>();
	
	// 如果要做朋友的request請求要在新增一個表格??===我現在把request放在friend表單裡 並改成兩個ManyToOne
	
//	@ManyToMany(mappedBy = "friends")
//	private Set<UserAccount> friendsinmap = new HashSet<UserAccount>();
//	
//	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinTable(name = "FriendList", joinColumns = {
//			@JoinColumn(name = "fk_UserAccount", referencedColumnName = "account_id") }, inverseJoinColumns = {
//					@JoinColumn(name = "fk_FriendAccount", referencedColumnName = "account_id") })
//	private Set<UserAccount> friends = new HashSet<UserAccount>();
	
	//從左邊找
	@JsonIgnore
	@OneToMany(mappedBy = "mainUser",fetch = FetchType.LAZY,cascade = CascadeType.ALL)	
	private Set<FriendList> friends = new HashSet<FriendList>();
	
	//被右邊找
	@JsonIgnore
	@OneToMany(mappedBy = "friends",fetch = FetchType.LAZY,cascade = CascadeType.ALL)	
	private Set<FriendList> beFriends = new HashSet<FriendList>();


	// UserFollowerForm=============================================================
	
	@ManyToMany(mappedBy = "followers")
	private Set<UserAccount> followersinmap = new HashSet<UserAccount>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "UserFollowerForm", joinColumns = {
			@JoinColumn(name = "fk_userAccount_id", referencedColumnName = "account_id") }, inverseJoinColumns = {
					@JoinColumn(name = "fk_followerAccount_id", referencedColumnName = "account_id") })
	private Set<UserAccount> followers = new HashSet<UserAccount>();



	// MessageBox=============================================================

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "netizenAccount",cascade = CascadeType.ALL)
	private List<MessageBox> netizens = new ArrayList<MessageBox>();
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "UserAccount",cascade = CascadeType.ALL)
	private List<MessageBox> msnBox = new ArrayList<MessageBox>();
	
	// Forum=============================================================

		@OneToMany(fetch = FetchType.LAZY,mappedBy = "userAccount",cascade = CascadeType.ALL)
		private List<Forum> forums = new ArrayList<Forum>();
		
	// ForumCollections=============================================================
	
	@ManyToMany(mappedBy = "colloctors")
	private Set<Forum> collectedforums = new HashSet<Forum>();


	// ForumMessageBox=============================================================

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "forumNetizenAccount",cascade = CascadeType.ALL)
	private List<ForumMessageBox> forumNetizens = new ArrayList<ForumMessageBox>();
	
	// ReplyMessageBox=============================================================

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "replyuserAccount",cascade = CascadeType.ALL)
	private List<ReplyMessage> replyNetizenAccounts = new ArrayList<ReplyMessage>();
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "commentUserAccount",cascade = CascadeType.ALL)
	private List<ReplyMessage> commentnetizenAccounts = new ArrayList<ReplyMessage>();
	
	// ForumReplyMessageBox=============================================================
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "forumReplyuserAccount",cascade = CascadeType.ALL)
	private List<ForumReplyMessage> forumReplyNetizenAccounts = new ArrayList<ForumReplyMessage>();
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "forumCommentUserAccount",cascade = CascadeType.ALL)
	private List<ForumReplyMessage> forumCommentnetizenAccounts = new ArrayList<ForumReplyMessage>();
	
	// =============================================================
  
  	/** 1個User可以有多個餐廳 **/
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "userAccount" , cascade = CascadeType.ALL)
	Set<Restaurant> Restaurant = new LinkedHashSet<Restaurant>();
	
	/** 1個User可以對多個餐廳留言表留言 **/
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "userAccount" , cascade = CascadeType.ALL)
	Set<RestaurantMessageBox> RestaurantMessageBox = new LinkedHashSet<RestaurantMessageBox>();
	
	/** 一個會員可以有多個回覆留言 **/
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "userAccount" , cascade = CascadeType.ALL)
	Set<RestaurantReplyMessage> RestaurantReplyMessage = new LinkedHashSet<RestaurantReplyMessage>();
	
	@Transient
	private String code;
	
	@Transient
	MultipartFile productImage;


	public UserAccount() {
	}

	

	public UserAccount(String accountIndex, String password) {
		super();
		this.accountIndex = accountIndex;
		this.password = password;
	}



	// =============================================================
	
	public String getAccountIndex() {
		return accountIndex;
	}


	public void setAccountIndex(String accountIndex) {
		this.accountIndex = accountIndex;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public UserDetail getUserAccountDetail() {
		return userAccountDetail;
	}



	public void setUserAccountDetail(UserDetail userAccountDetail) {
		this.userAccountDetail = userAccountDetail;
	}

	//好友系統用

	public Set<FriendList> getFriends() {
		return friends;
	}



	public void setFriends(Set<FriendList> friends) {
		this.friends = friends;
	}



	public Set<FriendList> getBeFriends() {
		return beFriends;
	}


	public void setBeFriends(Set<FriendList> beFriends) {
		this.beFriends = beFriends;
	}



	public Integer getAccountId() {
		return accountId;
	}



	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	
	//好友系統用
	

}
