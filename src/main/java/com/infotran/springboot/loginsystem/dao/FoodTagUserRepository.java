package com.infotran.springboot.loginsystem.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.FoodTagUser;
import com.infotran.springboot.commonmodel.UserAccount;

public interface FoodTagUserRepository extends JpaRepository<FoodTagUser, Integer> {
	
	@Query(value="select f FROM FoodTagUser f Where f.fkuserid= ?1")
	Set<FoodTagUser> getByFkuserid(UserAccount fkuserid);
	

//	@SuppressWarnings("unchecked")
//	@Modifying
//	FoodTagUser save(FoodTagUser fu);


}
