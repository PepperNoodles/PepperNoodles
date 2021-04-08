package com.infotran.springboot.common.model;

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
import javax.persistence.Table;
import com.infotran.springboot.loginsystem.model.UserAccount;
import com.infotran.springboot.shoppingmall.model.*;

@Entity
@Table(name="foodTag")
public class FoodTag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="foodTag_id")
	private int foodTagIid;
	
	@Column(name="FoodTagName")
	private String foodTagName;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(name="foodtag_user",joinColumns = {
			@JoinColumn(name="fk_foodTag_id",referencedColumnName = "foodTag_id")},inverseJoinColumns = {
			@JoinColumn(name="fk_userAccount_id",referencedColumnName = "account_id")})
	private Set<UserAccount> users = new HashSet<UserAccount>();

	



//	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//	@JoinTable(name="Forum_tag",joinColumns = {
//			@JoinColumn(name="fk_tag_id",referencedColumnName = "foodTag_id")},inverseJoinColumns = {
//			@JoinColumn(name="fk_forum_id",referencedColumnName = "forum_id")})
//	private Set<Forum> forums = new HashSet<Forum>();
	
	
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(name="FoodTag_Product",joinColumns = {
			@JoinColumn(name="fk_foodTag_id",referencedColumnName = "foodTag_id")},inverseJoinColumns = {
			@JoinColumn(name="fk_Product_id",referencedColumnName = "Product_id")})
	private Set<Product> Product = new HashSet<Product>();
	
	
	public FoodTag() {
		super();
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

	public Set<UserAccount> getUsers() {
		return users;
	}

	public void setUsers(Set<UserAccount> users) {
		this.users = users;
	}


	public Set<Product> getProduct() {
		return Product;
	}

	public void setProduct(Set<Product> product) {
		Product = product;
	}
	
	
	
}
