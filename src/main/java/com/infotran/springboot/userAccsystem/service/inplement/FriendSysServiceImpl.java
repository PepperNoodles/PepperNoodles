package com.infotran.springboot.userAccsystem.service.inplement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.FriendList;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.userAccsystem.repository.FriendSysRepository;
import com.infotran.springboot.userAccsystem.service.FriendSysService;

@Service
public class FriendSysServiceImpl implements FriendSysService {

	@Autowired
	FriendSysRepository friendSysRepository;	
	
	//找到好友請求
	@Override
	public List<FriendList> findByFriendship(UserAccount user, UserAccount friend, String friendship) {
		List<FriendList> friends = friendSysRepository.findByFriendship(user, friend, friendship);
		return friends;
	}
	
	@Override
	public FriendList save(FriendList friendList) {
		friendSysRepository.save(friendList);
		return friendList;
	}

	@Override
	public List<FriendList> findRelationFromUserAndFriend(UserAccount user, UserAccount friend) {
		return friendSysRepository.findRelationFromUserAndFriend(user, friend);
	}

	@Override
	public List<FriendList> findFriendByUserAndRelation(UserAccount user, String friendship) {
		List<FriendList> friends = friendSysRepository.findFriendByUserAndRelation(user, friendship);
		return friends;
	}

	@Override
	public List<FriendList> findFriendByFriendAndRelation(UserAccount user, String friendship) {
		
		return friendSysRepository.findFriendByFriendAndRelation(user, friendship);
	}
	
	
	
	

}
