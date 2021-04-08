package com.infotran.springboot.commonmodel;

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
	private byte[] eventPicture;
	
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

}
