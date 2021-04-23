package com.infotran.springboot.commonmodel;

import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;


@Entity
@Component
@Table(name = "menuDetail")
public class MenuDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MenuDetail_id")
	private Integer menuDetailId;
	
	@Column(name = "MenuDetail")
	private Blob dishImage;
	
	@Transient
	@Column(name = "fk_restaurant_id")
	private Integer restaurantId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_restaurant_id")
	private Restaurant restaurant;

	public Integer getMenuDetailId() {
		return menuDetailId;
	}

	public void setMenuDetailId(Integer menuDetailId) {
		this.menuDetailId = menuDetailId;
	}


	public Blob getDishImage() {
		return dishImage;
	}

	public void setDishImage(Blob dishImage) {
		this.dishImage = dishImage;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	

}
