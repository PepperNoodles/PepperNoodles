package com.infotran.springboot.commonmodel;

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*留言表*/

@Entity
@Component
@Table(name = "restaurantMessageBox")
public class RestaurantMessageBox {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RestaurantMessage_id")
	private Integer restaurantMessageId;
	
	//留言者
	@Transient
	@Column (name = "fk_netizen_account_id")
	private Integer netizenAccountId;   
	
	@Column(name = "Text")
	private String text;
	
	@Column(name = "Time")
	private Date time;
	
	@Column(name = "LikeAmount")
	private Integer likeAmount;
	
	@Column(name = "Score")
	private Integer score;
	
	/**被留言餐廳**/
	@Transient
	@Column(name = "fk_restaurant_id")
	private Integer restaurantId;
	
	/** 1個User可以對多個餐廳留言表留言 **/
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_netizen_account_id")
	private UserAccount userAccount;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_restaurant_id")
	private Restaurant restaurant;
	
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "restaurantMessageBox" , cascade = CascadeType.ALL)
	Set<RestaurantReplyMessage> restaurantMessage = new LinkedHashSet<RestaurantReplyMessage>();
	

	

	public Integer getRestaurantMessageId() {
		return restaurantMessageId;
	}

	public void setRestaurantMessageId(Integer restaurantMessageId) {
		this.restaurantMessageId = restaurantMessageId;
	}

	public Integer getNetizenAccountId() {
		return netizenAccountId;
	}

	public void setNetizenAccountId(Integer netizenAccountId) {
		this.netizenAccountId = netizenAccountId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getLikeAmount() {
		return likeAmount;
	}

	public void setLikeAmount(Integer likeAmount) {
		this.likeAmount = likeAmount;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
}
