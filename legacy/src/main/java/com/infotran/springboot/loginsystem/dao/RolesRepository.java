package com.infotran.springboot.loginsystem.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.infotran.springboot.commonmodel.Roles;


public interface RolesRepository extends JpaRepository<Roles, Integer> {
	
	//自訂email查詢單一使用者
	public Optional<Roles> findById(Integer id);

}
