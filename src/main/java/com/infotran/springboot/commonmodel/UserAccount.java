package com.infotran.springboot.commonmodel;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;


public class UserAccount {

	
	/** 1個User可以有多個餐廳 **/
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "userAccount" , cascade = CascadeType.ALL)
	Set<Restaurant> Restaurant = new LinkedHashSet<Restaurant>();
	
	/** 1個User可以對多個餐廳留言表留言 **/
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "userAccount" , cascade = CascadeType.ALL)
	Set<RestaurantMessageBox> RestaurantMessageBox = new LinkedHashSet<RestaurantMessageBox>();
	
	/** 一個會員可以有多個回覆留言 **/
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "userAccount" , cascade = CascadeType.ALL)
	Set<RestaurantReplyMessage> RestaurantReplyMessage = new LinkedHashSet<RestaurantReplyMessage>();
	
}
