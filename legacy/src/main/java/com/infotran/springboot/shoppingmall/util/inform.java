package com.infotran.springboot.shoppingmall.util;

public class inform {//傳出去給前台的bean 假的bean pojo [kitty,chrislo@gmail.com]
	
	private String text;//
	
	private String name;

	public String getText() {
		return text;
	}

	public void setText(String text) { // text ====>>getMessageText()
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
