package com.infotran.springboot.userAccsystem.service.inplement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.UserDetail;
import com.infotran.springboot.userAccsystem.repository.UserDetailRepository;
import com.infotran.springboot.userAccsystem.service.UserDetailService;

@Service
public class UserDetailServiceImpl implements UserDetailService {

	@Autowired
	UserDetailRepository UserDetailRepository;
	
	
	@Override
	public UserDetail get(Integer id) {
		Optional<UserDetail> userDetailOptional=UserDetailRepository.findById(id);
		
		if (userDetailOptional.isPresent())
			return userDetailOptional.get();
		else {
			return null;
		}	
	}


	@Override
	public List<UserDetail> finbyByNickName(String name) {
		List<UserDetail> friends = UserDetailRepository.findUserDetailLikename(name);
		return friends;
	}

}
