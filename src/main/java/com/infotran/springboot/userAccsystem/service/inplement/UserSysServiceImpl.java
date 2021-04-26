package com.infotran.springboot.userAccsystem.service.inplement;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.userAccsystem.repository.UserSysRepository;
import com.infotran.springboot.userAccsystem.service.UserSysService;

@Service
@Transactional
public class UserSysServiceImpl implements UserSysService {
	@Autowired
	UserSysRepository userSysRepository;
	
	@Override
	public UserAccount findByAccountIndex(String accountIndex) {
		UserAccount userAcc = userSysRepository.findByAccountIndex(accountIndex);
		return userAcc;
	}

	@Override
	public UserAccount get(Integer id) {
		UserAccount userAcc = userSysRepository.getOne(id);
		return userAcc;
	}
	
	
	public void update(UserAccount userAccount) {
		userSysRepository.save(userAccount);
	}

}
