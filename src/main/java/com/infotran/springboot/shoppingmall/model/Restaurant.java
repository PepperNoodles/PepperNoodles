package com.infotran.springboot.shoppingmall.model;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name="Restaurant")
@Component
public class Restaurant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Restaurant_id")
	private Integer RestaurantId;
	
	
	private String restaurantName;

	
	public Restaurant() {
	}

	public Integer getRestaurantId() {
		return RestaurantId;
	}


	public void setRestaurantId(Integer restaurantId) {
		RestaurantId = restaurantId;
	}


	public String getRestaurantName() {
		return restaurantName;
	}


	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	
	
}
