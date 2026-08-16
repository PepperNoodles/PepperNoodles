package com.infotran.springboot.companysystem.service;

import java.util.List;

import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;

public interface MenuDetailService {
	MenuDetail insert(MenuDetail menu);
	MenuDetail update(MenuDetail menu);
	void deleteById(Integer id);
	MenuDetail findById(Integer id);
	
	List<MenuDetail> getByRest(Restaurant rest);
}
