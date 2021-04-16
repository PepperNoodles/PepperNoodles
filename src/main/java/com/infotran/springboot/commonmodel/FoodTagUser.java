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
	private Integer fkuser;
	
	@Column(name="fk_foodTag_id")
	@Transient
	private Integer fkfoodtag;
	
	@ManyToOne(fetch = FetchType.LAZY/*, optional = false*/)
    @JoinColumn(name = "fk_userAccount_id"/*,insertable = false,updatable = false*/)
	@JsonBackReference
	private UserAccount  fkuserid;
	
	
	@ManyToOne(fetch = FetchType.LAZY/*, optional = false*/)
    @JoinColumn(name = "fk_foodTag_id"/*,insertable = false,updatable = false*/)
	@JsonBackReference
	private FoodTag  fkfoodtagid;
	

	public FoodTagUser() {
	}

	public Integer getFooTagUserId() {
		return FooTagUserId;
	}


	public void setFooTagUserId(Integer fooTagUserId) {
		FooTagUserId = fooTagUserId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getFkuser() {
		return fkuser;
	}

	public void setFkuser(Integer fkuser) {
		this.fkuser = fkuser;
	}

	public Integer getFkfoodtag() {
		return fkfoodtag;
	}

	public void setFkfoodtag(Integer fkfoodtag) {
		this.fkfoodtag = fkfoodtag;
	}

	public UserAccount getFkuserid() {
		return fkuserid;
	}

	public void setFkuserid(UserAccount fkuserid) {
		this.fkuserid = fkuserid;
	}

	public FoodTag getFkfoodtagid() {
		return fkfoodtagid;
	}

	public void setFkfoodtagid(FoodTag fkfoodtagid) {
		this.fkfoodtagid = fkfoodtagid;
	}

	
}

