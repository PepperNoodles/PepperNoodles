package com.infotran.springboot.userAccsystem.service;

import java.util.List;

import com.infotran.springboot.commonmodel.UserDetail;

public interface UserDetailService {
	
	UserDetail get(Integer id);
	
	List<UserDetail> finbyByNickName(String name);
}
