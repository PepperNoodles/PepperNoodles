package com.infotran.springboot.shoppingmall.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.model.OrderListView;

public interface OrderListRepository extends JpaRepository<OrderList, Integer> {

	
	@Query(value="SELECT dbo.OrderList.UUID, dbo.Product.ProductName, dbo.OrderList.OrderCreatedDate, dbo.OrderList.Status, dbo.OrderList.TotalCost "
				+ "FROM  dbo.OrderList INNER JOIN "
				+ "dbo.OrderDetail ON dbo.OrderList.Order_id = dbo.OrderDetail.fk_Order_id INNER JOIN "
				+ "dbo.userAccount ON dbo.OrderList.fk_userAccount_id = dbo.userAccount.account_id INNER JOIN "
				+ "dbo.Product ON dbo.OrderDetail.fk_Product_id = dbo.Product.Product_id "
				+ "WHERE (dbo.userAccount.acoount_index = ?1) "
				+ "ORDER BY dbo.OrderList.OrderCreatedDate DESC",nativeQuery=true)
	public ArrayList<OrderListView> findOrderList (String name);
 
	
	
	
}
