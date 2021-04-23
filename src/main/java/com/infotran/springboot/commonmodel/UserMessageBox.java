package com.infotran.springboot.commonmodel;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class UserMessageBox {
	
	
	private ArrayList<String> text ;

	public ArrayList<String> getText() {
		return text;
	}

	public void setText(ArrayList<String> text) {
		this.text = text;
	}
	
	public UserMessageBox() {
		
	}

}
