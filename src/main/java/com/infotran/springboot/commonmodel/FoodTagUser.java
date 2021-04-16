package com.infotran.springboot.commonmodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "foodtag_user")
@Component
public class FoodTagUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="food_UserAccount_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer FooTagUserId;
	
	@Column(name="fk_userAccount_id")
	@Transient
	private Integer fkuserid;

	@Column(name="fk_foodTag_id")
	@Transient
	private Integer fkfoodtagid;

	
	
	@ManyToOne(fetch = FetchType.LAZY/*, optional = false*/)
    @JoinColumn(name = "fk_userAccount_id"/*,insertable = false,updatable = false*/)
	@JsonBackReference
	private UserAccount  fkuser;
	
	
	@ManyToOne(fetch = FetchType.LAZY/*, optional = false*/)
    @JoinColumn(name = "fk_foodTag_id"/*,insertable = false,updatable = false*/)
	@JsonBackReference
	private FoodTag  fkfoodtag;
	

	public FoodTagUser() {
	}

	public Integer getFooTagUserId() {
		return FooTagUserId;
	}


	public void setFooTagUserId(Integer fooTagUserId) {
		FooTagUserId = fooTagUserId;
	}



	


	public UserAccount getFkuser() {
		return fkuser;
	}

	public void setFkuser(UserAccount fkuser) {
		this.fkuser = fkuser;
	}

	public FoodTag getFkfoodtag() {
		return fkfoodtag;
	}

	public void setFkfoodtag(FoodTag fkfoodtag) {
		this.fkfoodtag = fkfoodtag;
	}

	public Integer getFkuserid() {
		return fkuserid;
	}

	public void setFkuserid(Integer fkuserid) {
		this.fkuserid = fkuserid;
	}

	public Integer getFkfoodtagid() {
		return fkfoodtagid;
	}

	public void setFkfoodtagid(Integer fkfoodtagid) {
		this.fkfoodtagid = fkfoodtagid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

