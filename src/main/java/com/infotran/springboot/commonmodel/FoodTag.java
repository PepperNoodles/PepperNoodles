package com.infotran.springboot.commonmodel;


import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.infotran.springboot.shoppingmall.model.FoodTagProduct;

@Entity
@Table(name="foodTag")
@Component
public class FoodTag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="foodTag_id")
	private int foodTagIid;
	
	@Column(name="FoodTagName")
	private String foodTagName;
	
//	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//	@JoinTable(name="foodtag_user",joinColumns = {
//	@JoinColumn(name="fk_foodTag_id",referencedColumnName = "foodTag_id")},inverseJoinColumns = {

//	@JoinColumn(name="fk_userAccount_id",referencedColumnName = "account_id")})
//	private Set<UserAccount> users = new HashSet<UserAccount>();

//	@JoinColumn(name="fk_userAccount_id",referencedColumnName = "account_id")})
//	private Set<UserAccount> users = new HashSet<UserAccount>();

	@OneToMany(mappedBy = "fkfoodtagid",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<FoodTagUser> FoodTagUsers = new HashSet<FoodTagUser>();
	

	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(name="Forum_tag",joinColumns = {
			@JoinColumn(name="fk_tag_id" ,referencedColumnName = "foodTag_id")},inverseJoinColumns = {
			@JoinColumn(name="fk_forum_id",referencedColumnName = "forum_id")})
	private Set<Forum> forums = new HashSet<Forum>();
	
	/* 所屬的tag */
	@OneToMany(mappedBy = "fkFoodtagid",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<FoodTagProduct> Product = new HashSet<FoodTagProduct>();
	
	public FoodTag() {
	}

	public int getFoodTagIid() {
		return foodTagIid;
	}

	public void setFoodTagIid(int foodTagIid) {
		this.foodTagIid = foodTagIid;
	}

	public String getFoodTagName() {
		return foodTagName;
	}

	public void setFoodTagName(String foodTagName) {
		this.foodTagName = foodTagName;
	}

	public Set<Forum> getForums() {
		return forums;
	}

	public void setForums(Set<Forum> forums) {
		this.forums = forums;
	}

	public Set<FoodTagProduct> getProduct() {
		return Product;
	}

	public void setProduct(Set<FoodTagProduct> product) {
		Product = product;
	}

	public Set<FoodTagUser> getFoodTagUsers() {
		return FoodTagUsers;
	}

	public void setFoodTagUsers(Set<FoodTagUser> foodTagUsers) {
		FoodTagUsers = foodTagUsers;
	}
	
	
}
