package com.infotran.springboot.websocket.model;

public class MessageModel {
	
	private String message;
	private String fromLogin;
	
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
		return "MessageModel [message=" + message + ", fromLogin=" + fromLogin + "]";
	}
	
}
