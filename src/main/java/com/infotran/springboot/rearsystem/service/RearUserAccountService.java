package com.infotran.springboot.rearsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.loginsystem.dao.UserAccountRepository;
import com.infotran.springboot.rearsystem.dao.RearUserAccountRepository;


@Service
@Transactional
public class RearUserAccountService {
	
	@Autowired
	private UserAccountRepository accountRepository;
	
	@Autowired
	private RearUserAccountRepository rearaccountRepository;
	
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
	
	//取得所有使用者
	public ArrayList<UserAccount> findAccountList() {
		ArrayList<UserAccount> account = rearaccountRepository.findUserAccountList();
		return account;
	}
	
//	public ArrayList<UserAccount> findCompanyList(Integer companyDetailId) {
//		ArrayList<UserAccount> company = rearaccountRepository.findCompanyList(companyDetailId);
//		return company;
//	}
	
	//取得一般使用者清單
	public ArrayList<UserAccount> findAccountUserList() {
		ArrayList<UserAccount> account = rearaccountRepository.findAccountUserList();
		return account;
	}
	
	//取得企業清單
	public ArrayList<UserAccount> findCompanyUserList() {
		ArrayList<UserAccount> company = rearaccountRepository.findCompanyUserList();
		return company;
	}
	
	//模糊搜尋
	public List<UserAccount> findAccountLike(String accountIndex){
		return rearaccountRepository.findAccountLike(accountIndex);
	}

}
