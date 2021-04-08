package com.infotran.springboot.commonmodel;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	private String realname;
	private String phonenumber;
	private String userphotoname;
	private Blob userphoto;
	private String location;
	private String level;
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
	public String getUserphotoname() {
		return userphotoname;
	}
	public void setUserphotoname(String userphotoname) {
		this.userphotoname = userphotoname;
	}
	public Blob getUserphoto() {
		return userphoto;
	}
	public void setUserphoto(Blob userphoto) {
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
	
	
	
}
