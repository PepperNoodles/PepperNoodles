package com.infotran.springboot.userAccsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.UserDetail;

public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {
	@Query(value="from UserDetail Where nickName like concat('%',?1,'%')",nativeQuery = false)
	public List<UserDetail> findUserDetailLikename(String nickname);
}
