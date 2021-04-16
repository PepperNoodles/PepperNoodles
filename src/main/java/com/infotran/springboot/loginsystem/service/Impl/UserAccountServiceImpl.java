package com.infotran.springboot.loginsystem.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.exception.UserNotFoundException;
import com.infotran.springboot.loginsystem.dao.UserAccountRepository;
import com.infotran.springboot.loginsystem.service.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	
	@Autowired
    private UserAccountRepository accountRepository ;

	@Autowired
	private UserAccountRepository UserDao;
	
	public UserAccount getByName(String name) {
		Optional<UserAccount> uRep = accountRepository.findByAccountIndex(name);
		//System.out.println("test1:" + uRep.get());		
		
		if(uRep.isEmpty()) {
			throw new UserNotFoundException("Can't find User");
		}
		
		return uRep.get();
	}
	
	public UserAccount createUserProfiles(UserAccount userProfiles) {
		return accountRepository.save(userProfiles);
	}

	@Override
	public Integer save(UserAccount user) {
		System.out.println("fuck======================================================4.5");
		String name =user.getAccountIndex();
		UserAccount user2 = UserDao.isExist(name);
		System.out.println("fuck======================================================4.6");

		Integer flag = 0;
		if(user2!=null) {
			System.out.println("fuck======================================================4.notnull");

			return -1;
		}else if (user2==null) {
			System.out.println("fuck======================================================4.null");
			System.out.println(user.getAccountIndex());
			System.out.println(user.getFoodTagUsers().size());

			UserDao.save(user);
			System.out.println("fuck======================================================4.save");

			flag= 1;
		}
		return flag;
	}

	@Override
	public void delete(UserAccount user) {
		UserDao.delete(user);
	}

	@Override
	public UserAccount isExist(String AccountName) {
		return UserDao.isExist(AccountName);
	}

	@Override
	public void update(UserAccount user) {
		UserDao.save(user);
	}

	@Override
	public List<UserAccount> getAllMembers() {
		return UserDao.findAll();
	}

	@Override
	public UserAccount findbyAccount(String AccountName) {
		List<UserAccount> userlist = UserDao.findByAccount(AccountName);
		UserAccount useraccount = null;
		if (!userlist.isEmpty()) {
			useraccount = userlist.get(0);
		}
		return useraccount;
	}

	@Override
	public String checkUserAccount(String AccountName) {
		List<UserAccount> userlist = UserDao.findByAccount(AccountName);
		UserAccount useraccount = null;
		String username = "";
		if (!userlist.isEmpty()) {
			useraccount = userlist.get(0);
			username = useraccount.getAccountIndex();
		}
		return username;
	}

}
