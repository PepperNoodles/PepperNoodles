package com.infotran.springboot.commonmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "companyDetail")
public class CompanyDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Company_Detail_id")
	private Integer companyDetailId;
	
	@Column(name = "realname")
	private String realname;
	
	@Column(name = "phonenumber")
	private String phonenumber;
	
	@Column(name = "userphoto")
	private byte[] userphoto;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "level")
	private String level;
	
	@OneToOne(mappedBy = "companyDetail")
	private UserAccount userAccount;
	
	

	public Integer getCompanyDetailId() {
		return companyDetailId;
	}

	public void setCompanyDetailId(Integer companyDetailId) {
		this.companyDetailId = companyDetailId;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;

	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}


	public byte[] getUserphoto() {
		return userphoto;
	}

	public void setUserphoto(byte[] userphoto) {
		this.userphoto = userphoto;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	
	
	
}
