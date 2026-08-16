package com.infotran.springboot.userAccsystem.service.inplement;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.MessageBox;
import com.infotran.springboot.commonmodel.Roles;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.userAccsystem.repository.MessageBoxRepository;

@Service
@Transactional
public class MessageBoxServiceImpl {
	
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
	
	public List<MessageBox> findByUserAccount(UserAccount UserAccount){
		return messageBoxRepository.findByUserAccount(UserAccount);
	}
	

	public Integer save(MessageBox MessageBox) {
		
		if(MessageBox!=null) {
			messageBoxRepository.save(MessageBox);
			return 1;
		}
		return 0;

	}

	public Integer delete(MessageBox MessageBox) {
		if(MessageBox!=null) {
			messageBoxRepository.delete(MessageBox);
			return 1;
		}
		return 0;
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
