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
@Table(name="userDetail")
@Component
public class UserDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Detail_id")
	private int Detail_id;
	
	private String RealName;
	
	private String NickName;
	
	private String PhoneNumber;
	
	private String BirthDay;
	
	private Blob UserPhoto;
	
	private String Sex;
	
	private String Location;
	
	private int FoodPoint;
	
	@OneToOne(mappedBy = "useraccountdetail")
	private UserAccount useraccount;
	
	public UserDetail() {
	}

	public int getDetail_id() {
		return Detail_id;
	}

	public void setDetail_id(int detail_id) {
		Detail_id = detail_id;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getBirthDay() {
		return BirthDay;
	}

	public void setBirthDay(String birthDay) {
		BirthDay = birthDay;
	}


	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public int getFoodPoint() {
		return FoodPoint;
	}

	public void setFoodPoint(int foodPoint) {
		FoodPoint = foodPoint;
	}

	public UserAccount getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(UserAccount useraccount) {
		this.useraccount = useraccount;
	}

	public Blob getUserPhoto() {
		return UserPhoto;
	}

	public void setUserPhoto(Blob userPhoto) {
		UserPhoto = userPhoto;
	}
	
	
}
