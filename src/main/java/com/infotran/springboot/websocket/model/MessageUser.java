package com.infotran.springboot.websocket.model;

public class MessageUser {

	
	private String mUserAccountIndex;
	
	private Integer mUserDetailId;
	
	private String mUserAccountNickName;

	public MessageUser() {
		
	}

	public String getmUserAccountIndex() {
		return mUserAccountIndex;
	}

	public void setmUserAccountIndex(String mUserAccountIndex) {
		this.mUserAccountIndex = mUserAccountIndex;
	}

	public Integer getmUserDetailId() {
		return mUserDetailId;
	}

	public void setmUserDetailId(Integer mUserDetailId) {
		this.mUserDetailId = mUserDetailId;
	}

	public String getmUserAccountNickName() {
		return mUserAccountNickName;
	}

	public void setmUserAccountNickName(String mUserAccountNickName) {
		this.mUserAccountNickName = mUserAccountNickName;
	}
	
	
	
}
