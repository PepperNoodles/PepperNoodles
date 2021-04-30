package com.infotran.springboot.commonmodel;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.infotran.springboot.shoppingmall.model.Product;

@Entity
@Component
@Table(name = "restaurant", uniqueConstraints = { @UniqueConstraint(columnNames = { "restaurantAddress" }) })
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id")
	private Integer restaurantId;
	@Column(name = "restaurantName")
	private String restaurantName;
	@Column(name = "totalScore")
	private Integer totalScore;
	@Column(name = "restaurantAmount")
	private Integer rankAmount;

	@Column(name = "restaurantAddress")
	private String restaurantAddress;

	@Column(name = "restaurantContact")
	private String restaurantContact;

	@Column(name = "restaurantWebsite")
	private String restaurantWebsite;


	@JsonIgnore
	@Column(name = "restaurantPhoto")
	private Blob restaurantPhoto;

	// 新增修改時取圖的屬性
	@Transient
	@JsonIgnore
	MultipartFile productImage;

	@Column(name = "longitude")
	private BigDecimal longitude;

	@Column(name = "latitude")
	private BigDecimal latitude;

	@Transient
	@Column(name = "fk_userAccount_id")
	private Integer userAccountId;

	/** 1個User可以有多個餐廳 **/
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_userAccount_id")
	@JsonIgnore
	private UserAccount userAccount;

	/** 1個餐廳可以有多個菜單 **/
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
	@JsonIgnore
	Set<MenuDetail> Menus = new LinkedHashSet<MenuDetail>();

	/** 1個餐廳可以有多個留言 **/
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
	@JsonIgnore
	Set<RestaurantMessageBox> restaurantMessageBox = new LinkedHashSet<RestaurantMessageBox>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
	@JsonIgnore
	Set<EventList> eventList = new LinkedHashSet<EventList>();

	/** 一家餐廳有多個營業時間 **/
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurantBusinHourId", cascade = CascadeType.ALL)
	Set<RestaurantBusinHour> RestaurantBusinHour = new LinkedHashSet<RestaurantBusinHour>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "foodtag_restaurant", joinColumns = {
			@JoinColumn(name = "fk_restaurant_id", referencedColumnName = "Restaurant_id") }, inverseJoinColumns = {
					@JoinColumn(name = "fk_tag_id", referencedColumnName = "foodTag_id") })
	private Set<FoodTag> foodTag = new HashSet<FoodTag>();

	

	
	/*對應產品*/
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "restaurant",cascade =CascadeType.ALL)
	@JsonIgnore
	List<Product> products =new ArrayList<Product>();




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

	public Blob getRestaurantPhoto() {
		return restaurantPhoto;
	}

	public void setRestaurantPhoto(Blob restaurantPhoto) {
		this.restaurantPhoto = restaurantPhoto;
	}

	public MultipartFile getProductImage() {
		return productImage;
	}

	public void setProductImage(MultipartFile productImage) {
		this.productImage = productImage;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
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

	public Set<RestaurantBusinHour> getRestaurantBusinHour() {
		return RestaurantBusinHour;
	}

	public void setRestaurantBusinHour(Set<RestaurantBusinHour> restaurantBusinHour) {
		RestaurantBusinHour = restaurantBusinHour;
	}

	public Set<FoodTag> getFoodTag() {
		return foodTag;
	}

	public void setFoodTag(Set<FoodTag> foodTag) {
		this.foodTag = foodTag;
	}

}
