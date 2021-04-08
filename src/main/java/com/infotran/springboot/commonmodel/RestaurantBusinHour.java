package com.infotran.springboot.commonmodel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.crypto.Data;

import org.springframework.stereotype.Component;

/*營業時間*/

@Entity
@Component
@Table(name = "restaurantBusinHour")
public class RestaurantBusinHour {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RestaurantBusinHour_id")
	private Integer restaurantBusinHourId;
	
	@OneToOne(mappedBy = "Restaurant_id" , cascade = CascadeType.ALL)
	@Column(name = "fk_restaurant_id")
	private Integer restaurantId;
	
	private Data day;
	
	@Column(name = "open_time")
	private String openTime;
	
	@Column(name = "close_time")
	private String closeTime;
	
	@Column(name = "open_time_2nd")
	private String openTime2nd;
	
	@Column(name = "close_time_2nd")
	private String closeTime2nd;
	
	@Column(name = "open_time_3rd")
	private String openTime3rd;
	
	@Column(name = "close_time_3rd")
	private String closeTime3rd;

	public Integer getRestaurantBusinHourId() {
		return restaurantBusinHourId;
	}

	public void setRestaurantBusinHourId(Integer restaurantBusinHourId) {
		this.restaurantBusinHourId = restaurantBusinHourId;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Data getDay() {
		return day;
	}

	public void setDay(Data day) {
		this.day = day;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getOpenTime2nd() {
		return openTime2nd;
	}

	public void setOpenTime2nd(String openTime2nd) {
		this.openTime2nd = openTime2nd;
	}

	public String getCloseTime2nd() {
		return closeTime2nd;
	}

	public void setCloseTime2nd(String closeTime2nd) {
		this.closeTime2nd = closeTime2nd;
	}

	public String getOpenTime3rd() {
		return openTime3rd;
	}

	public void setOpenTime3rd(String openTime3rd) {
		this.openTime3rd = openTime3rd;
	}

	public String getCloseTime3rd() {
		return closeTime3rd;
	}

	public void setCloseTime3rd(String closeTime3rd) {
		this.closeTime3rd = closeTime3rd;
	}
	
	
	
}
