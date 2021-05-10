package com.infotran.springboot.rearMessage.service;

import java.util.ArrayList;
import java.util.List;

import com.infotran.springboot.commonmodel.RearMessageBox;
import com.infotran.springboot.commonmodel.UserAccount;

public interface RearMessageBoxService {
	RearMessageBox insert(RearMessageBox rearMessageBox);
	
	RearMessageBox findById(Integer id);
	
	
	List<RearMessageBox> getByUserAccount(UserAccount user);
	
	public ArrayList<RearMessageBox> findMessage();
	
//	public RearMessageBox findById1(Integer id);
	
	//發布訊息時間
	List<RearMessageBox> findNewMessageByTime(String todayTime);
	//回復訊息更新時間
	List<RearMessageBox> findUpdatedMessageByTime(String replyTime);
	

}
