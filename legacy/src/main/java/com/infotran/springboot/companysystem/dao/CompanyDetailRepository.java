package com.infotran.springboot.companysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.CompanyDetail;


public interface CompanyDetailRepository extends JpaRepository<CompanyDetail, Integer> {
	
}
