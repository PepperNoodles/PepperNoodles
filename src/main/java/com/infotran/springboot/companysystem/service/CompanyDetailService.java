package com.infotran.springboot.companysystem.service;

import java.util.List;

import com.infotran.springboot.commonmodel.CompanyDetail;
import com.infotran.springboot.commonmodel.UserAccount;

public interface CompanyDetailService {
	CompanyDetail insert(CompanyDetail comBean);
	CompanyDetail update(CompanyDetail comBean);
	void deleteById(Integer id);
	CompanyDetail findById(Integer id);
	List<CompanyDetail> getAllCompanys();

}
