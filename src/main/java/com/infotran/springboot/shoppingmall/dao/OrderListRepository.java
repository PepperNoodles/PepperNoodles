package com.infotran.springboot.shoppingmall.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;

public interface OrderListRepository extends JpaRepository<OrderList, Integer> {
	
	
	
	@Query(value="select orlist from OrderList orlist where orlist.user.accountId =?1")
	public ArrayList<OrderList> findOrderList (Integer userid);
 
	
	@Query(value="select orlist from OrderList orlist where orlist.uuid=?1")
	public OrderList findOrderLisyByUUID(long uuid);
	
	@Query(value="select odetail from OrderDetail odetail where odetail.orderlist.orderId=?1")
	public ArrayList<OrderDetail> findOrderDetailByfkOrderId(Integer orderid);
	
	
	public Optional<OrderList> findById(Integer orderlistid);
	
	@Query(value="select sum(amount) from OrderDetail where product.productId=?1")
	public Integer countSumByProductId(Integer fkproductid);
	
	
	@Query(value="select orlist from OrderList orlist where orlist.orderCreatedDate BETWEEN ?1 and ?2 and orlist.status='已付款'")
	public ArrayList<OrderList> findOrderListByDateBetween(Date stratDate,Date endDate);
	
	@Query(value="select sum(totalCost) from OrderList orlist where convert(varchar,orlist.orderCreatedDate,111) = ?1 and orlist.status='已付款'")
	public Integer sumBySameDateWithConvertTo111(String date);
	
	@Query(value="select orlist from OrderList orlist where convert(varchar,orlist.orderCreatedDate,111) >= ?1 and convert(varchar,orlist.orderCreatedDate,111) <= ?2 and orlist.status='已付款'")
	public ArrayList<OrderList> findOrderListByPast7or3Days(String startDate,String endDate);
	
	@Query(value="select orlist from OrderList orlist where convert(varchar,orlist.orderCreatedDate,111) = ?1")
	public ArrayList<OrderList> findOrderListArrayBySameDate(String Date);
}
