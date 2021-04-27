package com.infotran.springboot.rearsystem.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.loginsystem.dao.UserAccountRepository;


@Service
@Transactional
public class RearUserAccountService {
	
	@Autowired
	private UserAccountRepository accountRepository;
	
	public UserAccount insert(UserAccount userAccount) {
		return accountRepository.save(userAccount);
	}
	
	public UserAccount update(UserAccount userAccount) {
		return accountRepository.save(userAccount);
	}
	
	public void deleteById(Integer id) {
		accountRepository.deleteById(id);
	}
	
	
	public UserAccount finById(Integer id) {
		Optional<UserAccount> userAccountOptional = accountRepository.findById(id);
		return userAccountOptional.get();
	}
	
	
	
	
	

}
