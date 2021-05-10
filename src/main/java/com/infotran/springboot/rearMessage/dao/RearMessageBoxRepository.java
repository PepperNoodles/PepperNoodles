package com.infotran.springboot.rearMessage.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.RearMessageBox;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.UserAccount;

public interface RearMessageBoxRepository extends JpaRepository<RearMessageBox, Integer> {
	
	@Query(value="from RearMessageBox r where r.userAccount = :userAccount")
	List<RearMessageBox> getByUserAccount(UserAccount userAccount,Pageable page);
	
//	@Query(value="from RearMessageBox r where r.restaurant = :restaurant and userAccount= :user")
//	List<RearMessageBox> getByRestAndUser(Restaurant restaurant,UserAccount user,Pageable page);
	
	
	//取得Message
	@Query(value="select message from RearMessageBox message where message.userAccount != null")
	public ArrayList<RearMessageBox> findMessage();
	
	//發布訊息時間 111是對應日期格式 (上架中) 0對應資料表的不是用false
	@Query(value="select message from RearMessageBox message where convert(nvarchar,message.time,111) =?1 and message.condition = 0")
	public List<RearMessageBox> findNewMessageByTime(String todayTime);
	
	//回復訊息更新時間 (下架中) 1對應資料表的不是用true
	@Query(value="select message from RearMessageBox message where convert(nvarchar,message.updateTime,111) >= ?1 and message.condition =1")
	public List<RearMessageBox> findUpdatedMessageByTime(String replyTime);
}
