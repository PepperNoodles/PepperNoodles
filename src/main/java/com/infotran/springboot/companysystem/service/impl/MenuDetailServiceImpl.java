package com.infotran.springboot.companysystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.dao.MenuDetailRepository;
import com.infotran.springboot.companysystem.service.MenuDetailService;

@Service
public class MenuDetailServiceImpl implements MenuDetailService {
	
	@Autowired
	MenuDetailRepository MenuDetailDao;

	@Override
	public MenuDetail insert(MenuDetail menu) {
		return MenuDetailDao.save(menu);
	}

	@Override
	public MenuDetail update(MenuDetail menu) {
		return MenuDetailDao.save(menu);
	}

	@Override
	public void deleteById(Integer id) {
		MenuDetailDao.deleteById(id);
	}

	@Override
	public MenuDetail findById(Integer id) {
		Optional<MenuDetail> menuDetailOptional = MenuDetailDao.findById(id);
		MenuDetail menuDetail = null;
		if (menuDetailOptional.isPresent()) {
			menuDetail = menuDetailOptional.get();
		} else {
			throw new RuntimeException("menu(id=" + id + ")不存在");
		}
		return menuDetail;
	}

	@Override
	public List<MenuDetail> getByRest(Restaurant rest) {
		Pageable pageable = PageRequest.of(0, 200, Sort.Direction.ASC, "menuDetailId");
		List<MenuDetail> menudetail = MenuDetailDao.getByRest(rest, pageable);
		return menudetail;
	}

}
