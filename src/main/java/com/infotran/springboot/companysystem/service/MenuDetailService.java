package com.infotran.springboot.companysystem.service;

import com.infotran.springboot.commonmodel.MenuDetail;

public interface MenuDetailService {
	MenuDetail insert(MenuDetail menu);
	MenuDetail update(MenuDetail menu);
	void deleteById(Integer id);
	MenuDetail findById(Integer id);
}
