package com.infotran.springboot.loginsystem.service.Impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.infotran.springboot.loginsystem.dao.UserAccountRepository;
import com.infotran.springboot.loginsystem.model.UserAccount;
import com.infotran.springboot.loginsystem.service.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {

	@Autowired
	private UserAccountRepository UserDao;

	@Override
	public Integer save(UserAccount user) {
		String name =user.getAccount();
		UserAccount user2 = UserDao.isExist(name);
		Integer flag = 0;
		if(user2!=null) {
			return -1;
		}else if (user2==null) {
			UserDao.save(user);
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
			username = useraccount.getAccount();
		}
		return username;
	}

}
