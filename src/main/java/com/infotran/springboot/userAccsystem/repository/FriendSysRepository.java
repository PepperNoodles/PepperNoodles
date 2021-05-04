package com.infotran.springboot.userAccsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.FriendList;
import com.infotran.springboot.commonmodel.UserAccount;



public interface FriendSysRepository extends JpaRepository<FriendList,Integer> {
	@Query(value="from FriendList f Where f.mainUser =?1 and f.friends=?2 and f.friendship=?3 ",nativeQuery = false)
	public List<FriendList> findByFriendship(UserAccount user,UserAccount friend,String friendship);
	
	
	@Query(value="from FriendList f Where f.mainUser =?1 and f.friends=?2",nativeQuery = false)
	public List<FriendList> findRelationFromUserAndFriend(UserAccount user,UserAccount friend);
	
	@Query(value="from FriendList f Where f.mainUser =?1 and f.friendship=?2",nativeQuery = false)
	public List<FriendList> findFriendByUserAndRelation(UserAccount user,String friendship);
	
	@Query(value="from FriendList f Where f.friends =?1 and f.friendship=?2",nativeQuery = false)
	public List<FriendList> findFriendByFriendAndRelation(UserAccount user,String friendship);
	
	@Query(value="from FriendList f Where f.mainUser =?1 and f.friendship=?2",nativeQuery = false)
	public List<FriendList> findFriendListFromUser(UserAccount user,String friendship);
}
