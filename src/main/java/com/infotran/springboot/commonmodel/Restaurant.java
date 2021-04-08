package com.infotran.springboot.commonmodel;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "restaurant")
public class Restaurant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Restaurant_id")
	private Integer restaurantId;
	
	private String restaurantName;
	
	private Integer totalScore;
	
	private Integer rankAmount;
	
	@Column(name = "RestaurantAddress")
	private String restaurantAddress;
	
	@Column(name = "RestaurantContact")
	private String restaurantContact;

	@Column(name = "RestaurantWebsite")
	private String restaurantWebsite;

	@Column(name = "RestaurantPhotoName")
	private byte[] restaurantPhotoName;
	
	@Column(name = "Longitude")
	private String longitude;
	
	@Column(name = "Longitude")
	private String latitude;
	
	@Transient
	@Column(name = "fk_userAccount_id")
	private Integer userAccountId;
	
	/** 1個User可以有多個餐廳 **/
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_userAccount_id")  
	private UserAccount userAccount;
	
	/** 1個餐廳可以有多個菜單 **/
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "restaurant" , cascade = CascadeType.ALL)
	Set<MenuDetail> Menus = new LinkedHashSet<MenuDetail>();
	
	/** 1個餐廳可以有多個留言 **/
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "restaurant" , cascade = CascadeType.ALL)
	Set<RestaurantMessageBox> restaurantMessageBox = new LinkedHashSet<RestaurantMessageBox>();
	
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "restaurant" , cascade = CascadeType.ALL)
	Set<EventList> eventList = new LinkedHashSet<EventList>();
	
	@ManyToMany
	@JoinTable(name = "foodtag_restaurant",joinColumns = {
			@JoinColumn(name="fk_restaurant_id",referencedColumnName="Restaurant_id")},inverseJoinColumns = {
					@JoinColumn(name="fk_tag_id",referencedColumnName = "Tag_id")})
	private Set<FoodTag> foodTag = new HashSet<FoodTag>();

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getRankAmount() {
		return rankAmount;
	}

	public void setRankAmount(Integer rankAmount) {
		this.rankAmount = rankAmount;
	}

	public String getRestaurantAddress() {
		return restaurantAddress;
	}

	public void setRestaurantAddress(String restaurantAddress) {
		this.restaurantAddress = restaurantAddress;
	}

	public String getRestaurantContact() {
		return restaurantContact;
	}

	public void setRestaurantContact(String restaurantContact) {
		this.restaurantContact = restaurantContact;
	}

	public String getRestaurantWebsite() {
		return restaurantWebsite;
	}

	public void setRestaurantWebsite(String restaurantWebsite) {
		this.restaurantWebsite = restaurantWebsite;
	}

	public byte[] getRestaurantPhotoName() {
		return restaurantPhotoName;
	}

	public void setRestaurantPhotoName(byte[] restaurantPhotoName) {
		this.restaurantPhotoName = restaurantPhotoName;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Integer getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Integer userAccountId) {
		this.userAccountId = userAccountId;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Set<MenuDetail> getMenus() {
		return Menus;
	}

	public void setMenus(Set<MenuDetail> menus) {
		Menus = menus;
	}

	public Set<RestaurantMessageBox> getRestaurantMessageBox() {
		return restaurantMessageBox;
	}

	public void setRestaurantMessageBox(Set<RestaurantMessageBox> restaurantMessageBox) {
		this.restaurantMessageBox = restaurantMessageBox;
	}

	public Set<EventList> getEventList() {
		return eventList;
	}

	public void setEventList(Set<EventList> eventList) {
		this.eventList = eventList;
	}

	public Set<FoodTag> getFoodTag() {
		return foodTag;
	}

	public void setFoodTag(Set<FoodTag> foodTag) {
		this.foodTag = foodTag;
	}
	
}
