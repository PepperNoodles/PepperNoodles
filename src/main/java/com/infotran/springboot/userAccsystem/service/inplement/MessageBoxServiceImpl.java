package com.infotran.springboot.personalpagesystem.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.MessageBox;
import com.infotran.springboot.commonmodel.Roles;
import com.infotran.springboot.personalpagesystem.dao.MessageBoxRepository;

@Service
public class MessageBoxImpl {
	
	@Autowired
	private MessageBoxRepository messageBoxRepository;

	public MessageBox findById(Integer id){
		Optional<MessageBox> messageBox = messageBoxRepository.findById(id);
		
		if(messageBox!=null) {
			return messageBox.get();

		}else {
			System.out.println("cant find messageBox!!!!");

			return null;
		}
		
	}
	

	public Integer save(Roles role) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Roles role) {
		// TODO Auto-generated method stub

	}

	public Roles findbyRoleName(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String checkRoles(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Roles isExist(String W) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(Roles role) {
		// TODO Auto-generated method stub

	}

	public List<Roles>  getAllRoles() {
		// TODO Auto-generated method stub
		return null;
	}

}
