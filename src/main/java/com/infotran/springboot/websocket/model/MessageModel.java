package com.infotran.springboot.websocket.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MessageModel {
	
	private String message;
	private String fromLogin;
	private String to;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss",locale = "Asia/Taipei", timezone = "GMT+8")
	private Date date;
	


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public MessageModel() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromLogin() {
		return fromLogin;
	}

	public void setFromLogin(String fromLogin) {
		this.fromLogin = fromLogin;
	}

	@Override
	public String toString() {
		return "MessageModel [message=" + message + ", fromLogin=" + fromLogin + "to="+to+ "date="+date+"]";
	}
	
}
