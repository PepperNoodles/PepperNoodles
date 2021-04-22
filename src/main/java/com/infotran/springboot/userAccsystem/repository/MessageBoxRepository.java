package com.infotran.springboot.personalpagesystem.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.MessageBox;

public interface MessageBoxRepository extends JpaRepository<MessageBox, Integer> {

//	Optional<MessageBox> findByUserMessageId(Integer id);

	
	
}
