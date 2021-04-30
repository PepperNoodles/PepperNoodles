package com.infotran.springboot.commonmodel;

import java.sql.Blob;
import java.sql.Date;

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

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Component
@Table(name = "eventList")
public class EventList {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name = "Event_id")
	private Integer eventId;
	
	@Column(name = "EventName")
	private String eventName;
	
	@Column(name = "Content")
	private String content;
	
	@Column(name = "EventPicture")
	@JsonIgnore
	private Blob eventPicture;
	
	@Column(name = "EventStartDate")
	private Date eventStartDate;
	
	@Column(name = "EventEndDate")
	private Date eventEndDate;
	
	@Transient
	@Column(name = "fk_Restaurant_id")
	private Integer restaurantId;
	
	@ManyToOne
	@JoinColumn(name = "fk_Restaurant_id")
	private Restaurant restaurant;

	
	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Blob getEventPicture() {
		return eventPicture;
	}

	public void setEventPicture(Blob eventPicture) {
		this.eventPicture = eventPicture;
	}

	public Date getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(Date eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public Date getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
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
