package com.infotran.springboot.rearMessage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.RearMessageBox;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.rearMessage.dao.RearMessageBoxRepository;
import com.infotran.springboot.rearMessage.service.RearMessageBoxService;

@Service
public class RearMessageBoxServiceImpl implements RearMessageBoxService{
	
	@Autowired
	RearMessageBoxRepository rearMessageBoxRepository;

	@Override
	public RearMessageBox insert(RearMessageBox rearMessageBox) {		
		return rearMessageBoxRepository.save(rearMessageBox);
	}

	@Override
	public RearMessageBox findById(Integer id) {
		Optional<RearMessageBox> RearMessageBoxOptional = rearMessageBoxRepository.findById(id);
		RearMessageBox rearMessageBox = null;
		if (RearMessageBoxOptional.isPresent()) {
			rearMessageBox = RearMessageBoxOptional.get();
		} else {
			throw new RuntimeException("rearMessageBox(id=" + id + ")不存在");
		}
		return rearMessageBox;
	}

	@Override
	public List<RearMessageBox> getByUserAccount(UserAccount user) {
		Pageable pageable = PageRequest.of(0, 200, Sort.Direction.ASC, "rearMessageId");
		List<RearMessageBox> rearMessageBox = rearMessageBoxRepository.getByUserAccount(user, pageable);
		return rearMessageBox;
	}

	@Override
	//取得Message
	public ArrayList<RearMessageBox> findMessage() {
		ArrayList<RearMessageBox> message = rearMessageBoxRepository.findMessage();
		return message;
	}

	//發布訊息時間
	@Override
	public List<RearMessageBox> findNewMessageByTime(String todayTime) {		
		return rearMessageBoxRepository.findNewMessageByTime(todayTime);
	}

	//回復訊息更新時間
	@Override
	public List<RearMessageBox> findUpdatedMessageByTime(String replyTime) {		
		return rearMessageBoxRepository.findUpdatedMessageByTime(replyTime);
	}

	//找訊息
//	@Override
//	public RearMessageBox findById1(Integer id) {
//		Optional<RearMessageBox> messageOptional = rearMessageBoxRepository.findById(id);
//		return messageOptional.get();
//	}

}
