package com.infotran.springboot.loginsystem.service;

import java.util.List;
import java.util.Optional;

import com.infotran.springboot.commonmodel.Roles;


public interface RolesService {

	public Roles findById(Integer id);
	
	// 存檔
	public Integer save(Roles role);

	void delete(Roles role);

	// 自訂查詢
	public Roles findbyRoleName(String roleName);
	
	public String checkRoles(String roleName);

	// 自訂布林查詢使用者是否存在
	public Roles isExist(String W);

	void update(Roles role);

	List<Roles> getAllRoles();
}
