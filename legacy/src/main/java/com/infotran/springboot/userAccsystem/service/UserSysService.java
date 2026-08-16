package com.infotran.springboot.userAccsystem.service;

import com.infotran.springboot.commonmodel.UserAccount;

public interface UserSysService {
	UserAccount findByAccountIndex(String accountIndex);
	UserAccount get(Integer id);

}
