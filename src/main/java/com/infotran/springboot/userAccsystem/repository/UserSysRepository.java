package com.infotran.springboot.userAccsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.UserAccount;

public interface UserSysRepository extends JpaRepository<UserAccount, Integer> {

}
