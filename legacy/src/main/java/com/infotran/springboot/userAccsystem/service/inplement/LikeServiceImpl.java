package com.infotran.springboot.userAccsystem.service.inplement;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.Like;
import com.infotran.springboot.commonmodel.MessageBox;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.userAccsystem.repository.LikeRepository;

@Service
@Transactional
public class LikeServiceImpl  {
	
	@Autowired
	LikeRepository likeRepository;
	
	
	
	public Integer save(UserAccount userAccount, MessageBox msn) {
		Integer flag = 0;
		Like like =likeRepository.getByUserAccountAndMessageBox(userAccount, msn);
		if(like==null) {
			System.out.println("沒被讚過啦!!!!!!!!!!!!!!!!!!!");
			Like liketoSave = new Like();
			liketoSave.setFkMSNId(msn);
			liketoSave.setFkUAId(userAccount);
			likeRepository.save(liketoSave);
			flag= 1;
		}else {
			flag= 0;
			System.out.println("被被被被被被被被被被被被被被被被被被被被被被被被被被被被被被被被被讚過了啦!!!!!!!!!!!!!!!!!!!");
		}
		return flag;
	}
	
	public Integer delete(UserAccount userAccount, MessageBox msn) {
		Integer flag = 0;
		Like like =likeRepository.getByUserAccountAndMessageBox(userAccount, msn);
		if(like!=null) {
			System.out.println("沒被讚過啦!!!!!!!!!!!!!!!!!!!要刪掉了喔!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			likeRepository.delete(like);
			flag= 1;
		}else {
			flag= 0;
			System.out.println("沒被讚過是要收回什麼讚???????????????????????????");
		}
		return flag;
	}

}
