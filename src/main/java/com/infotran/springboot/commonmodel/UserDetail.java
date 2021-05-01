package com.infotran.springboot.commonmodel;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="userDetail")
@Component
public class UserDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_Detail_id")
	private Integer useretailId;
	
	@Column(name = "realname")
	private String realName;
	
	@Column(name = "nickname")
	private String nickName;
	
	@Column(name = "phonenumber")
	private String phoneNumber;
	
	@Column(name = "birthday")
	private String birthDay;
	
	@Column(name="userphotoname")
	private String userPhotoName;
	
	@JsonIgnore
	@Column(name = "userphoto")
	private Blob userPhoto;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "fk_purse_id")
	private Integer purseID;
	

	@JsonIgnore
	@OneToOne(mappedBy = "userAccountDetail"/*,fetch = FetchType.LAZY*/)
	private UserAccount userAccount;
	
	public UserDetail() {
	}

	public Integer getUseretailId() {
		return useretailId;
	}

	public void setUseretailId(Integer useretailId) {
		this.useretailId = useretailId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public Blob getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(Blob userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getPurseID() {
		return purseID;
	}

	public void setPurseID(Integer purseID) {
		this.purseID = purseID;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserPhotoName() {
		return userPhotoName;
	}

	public void setUserPhotoName(String userPhotoName) {
		this.userPhotoName = userPhotoName;
	}
	
	

}
