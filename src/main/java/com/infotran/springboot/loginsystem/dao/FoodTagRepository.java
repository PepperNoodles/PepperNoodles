package com.infotran.springboot.loginsystem.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import com.infotran.springboot.commonmodel.FoodTag;

public interface FoodTagRepository extends JpaRepository<FoodTag, Integer> {
	
	FoodTag findByFoodTagName(String name);
	
	FoodTag findByFoodTagIid( Integer id) ;
	
	//自訂布林查詢使用者是否存在
	@Query("select u FROM FoodTag u Where u.foodTagIid= ?1")
	 boolean isExist(Integer id);
	
//	@Query(value="SELECT ft " +
//			"FROM  FoodTag ft,FoodTagUser ftu inner join " +
//			"FoodTag ft ON ftu.fkfoodtagid = ft.FoodTagUsers.fkfoodtagid "+
//			"WHERE  (ftu.fkuserid.accountId = ?1) ")
//	 Set<FoodTag> getUserInFoodtags(Integer id);
//


}
