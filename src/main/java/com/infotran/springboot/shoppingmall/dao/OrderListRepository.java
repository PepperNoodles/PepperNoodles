package com.infotran.springboot.shoppingmall.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.infotran.springboot.shoppingmall.model.OrderList;

public interface OrderListRepository extends JpaRepository<OrderList, Integer> {

	
	@Query(value="select orlist from OrderList orlist where orlist.user.accountId =?1")
	public ArrayList<OrderList> findOrderList (Integer userid);
 
	
	
	
}
