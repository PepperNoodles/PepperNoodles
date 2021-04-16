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

import org.springframework.stereotype.Component;

@Entity
@Table(name="friendlist")
@Component
public class FriendList {

	@Id
	@Column(name="Friend_Relation_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer friendRelationId;
	
	@Transient
	@Column(name="fk_UserAccount")
	private Integer fkUserAccount;
	
	//從friend list找往左找
	@ManyToOne
	@JoinColumn(name="fk_UserAccount")
	private UserAccount mainUser; 
	
	@Transient
	@Column(name="fk_FriendAccount")
	private Integer fkFriendAccount;
	
	//從friend list找往右邊找
	@ManyToOne
	@JoinColumn(name="fk_FriendAccount")
	private UserAccount friends;
	
	@Column(name="friendship")
	private String friendship;

	public Integer getFriendRelationId() {
		return friendRelationId;
	}

	public void setFriendRelationId(Integer friendRelationId) {
		this.friendRelationId = friendRelationId;
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

	public Integer getFkFriendAccount() {
		return fkFriendAccount;
	}

	public void setFkFriendAccount(Integer fkFriendAccount) {
		this.fkFriendAccount = fkFriendAccount;
	}

	public UserAccount getFriends() {
		return friends;
	}

	public void setFriends(UserAccount friends) {
		this.friends = friends;
	}

	public String getFriendship() {
		return friendship;
	}

	public void setFriendship(String friendship) {
		this.friendship = friendship;
	}
	
	
}
