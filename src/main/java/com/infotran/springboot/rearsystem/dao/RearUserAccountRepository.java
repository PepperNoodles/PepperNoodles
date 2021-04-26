package com.infotran.springboot.rearsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.UserAccount;

public interface RearUserAccountRepository extends JpaRepository<UserAccount, String> {

}
