package com.infotran.springboot.companysystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.CompanyDetail;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.dao.CompanyDetailRepository;
import com.infotran.springboot.companysystem.service.CompanyDetailService;

@Service
public class CompanyDetailServiceImpl implements CompanyDetailService {
	
	@Autowired
	CompanyDetailRepository ComDetailDao;
	
	@Override
	public CompanyDetail insert(CompanyDetail comBean) {
		return ComDetailDao.save(comBean);
	}

	@Override
	public CompanyDetail update(CompanyDetail comBean) {
		return ComDetailDao.save(comBean);
	}

	@Override
	public void deleteById(Integer id) {
		ComDetailDao.deleteById(id);
	}

	@Override
	public CompanyDetail findById(Integer id) {
		Optional<CompanyDetail> comDetailOptional = ComDetailDao.findById(id);
		CompanyDetail comDetail = null;
		if (comDetailOptional.isPresent()) {
			comDetail = comDetailOptional.get();
		} else {
			throw new RuntimeException("Company(id=" + id + ")不存在");
		}
		return comDetail;
	}

	@Override
	public List<CompanyDetail> getAllCompanys() {
		return ComDetailDao.findAll();
	}



}
