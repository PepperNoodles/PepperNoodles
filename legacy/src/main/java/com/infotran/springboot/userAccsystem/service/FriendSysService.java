package com.infotran.springboot.userAccsystem.service;

import java.util.List;

import com.infotran.springboot.commonmodel.FriendList;
import com.infotran.springboot.commonmodel.UserAccount;

public interface FriendSysService {
	
	public List<FriendList> findByFriendship(UserAccount user,UserAccount friend,String friendship);
	
	public List<FriendList> findRelationFromUserAndFriend(UserAccount user,UserAccount friend);
	
	public List<FriendList> findFriendByUserAndRelation(UserAccount user,String friendship);
	
	public List<FriendList> findFriendByFriendAndRelation(UserAccount user,String friendship);
	
	public FriendList save(FriendList friendList);
	
}
