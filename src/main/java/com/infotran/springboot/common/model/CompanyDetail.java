package com.infotran.springboot.common.model;

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
@Table(name="companyDetail")
@Component
public class CompanyDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="company_Detail_id")
	private int companyDetailId;
	
	@Column(name="realname")
	private String realName;
	
	@Column(name="phonenumber")
	private String phonenumber;
	
	@Column(name="userphotoname")
	private String userPhotoName;
	
	@Column(name="userphoto")
	private Blob userPhoto;
	
	@Column(name="location")
	private String location;
	
	
	@OneToOne(mappedBy = "companyDetail")
	private UserAccount userAccount;
	
	public CompanyDetail() {
	}

	public int getCompanyDetailId() {
		return companyDetailId;
	}

	public void setCompanyDetailId(int companyDetailId) {
		this.companyDetailId = companyDetailId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getUserPhotoName() {
		return userPhotoName;
	}

	public void setUserPhotoName(String userPhotoName) {
		this.userPhotoName = userPhotoName;
	}

	public Blob getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(Blob userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

}
