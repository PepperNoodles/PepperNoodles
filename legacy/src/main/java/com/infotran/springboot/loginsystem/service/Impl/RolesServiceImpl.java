package com.infotran.springboot.loginsystem.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.Roles;
import com.infotran.springboot.loginsystem.dao.RolesRepository;
import com.infotran.springboot.loginsystem.service.RolesService;

@Service
public class RolesServiceImpl implements RolesService {
	
	@Autowired
	private RolesRepository rolesRepository;

	@Override
	public Roles findById(Integer id){
		Optional<Roles> role = rolesRepository.findById(id);
		
		if(role!=null) {
			return role.get();

		}else {
			System.out.println("cant find roll!!!!");

			return null;
		}
		
	}
	
	@Override
	public Integer save(Roles role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Roles role) {
		// TODO Auto-generated method stub

	}

	@Override
	public Roles findbyRoleName(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String checkRoles(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Roles isExist(String W) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Roles role) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Roles>  getAllRoles() {
		// TODO Auto-generated method stub
		return null;
	}

}
