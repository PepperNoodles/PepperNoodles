package com.infotran.springboot.rearMessage.service.impl;

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

}
