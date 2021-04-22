package com.infotran.springboot.shoppingmall.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.model.Product;
import com.infotran.springboot.shoppingmall.service.OrderListService;
import com.infotran.springboot.shoppingmall.service.ShoppingMallService;
import com.infotran.springboot.shoppingmall.util.snowfFlakeUUID;

@Controller
public class checkOutController {
	
	@Autowired
	ShoppingMallService shopservice;
	
	@Autowired
	OrderListService orlistservice;
	
	@Autowired
	snowfFlakeUUID uuidgen;

	@PostMapping(value="/checkoutURL")
	public @ResponseBody String urlToCheckOut(@RequestPart("idlist")String toId,@RequestPart("amountlist")String toAmount) throws Exception {
		ArrayList<Integer> idlist =  new ObjectMapper().readValue(toId, new TypeReference<ArrayList<Integer>>() {});
		ArrayList<Integer> amountlist =  new ObjectMapper().readValue(toAmount, new TypeReference<ArrayList<Integer>>() {});
		Integer len = idlist.size();
		OrderDetail odetail = null;
		OrderList olist = new OrderList();
		Set <OrderDetail> odset = new LinkedHashSet<OrderDetail>();
		for (int i=0;i<len;i++) {
			odetail = new OrderDetail();
			Product product = shopservice.findById(idlist.get(i));
			odetail.setProduct(product);
			odetail.setAmount(amountlist.get(i));
			odset.add(odetail);
			odetail.setOrderlist(olist);
		}
		olist.setOdetails(odset);
		orlistservice.save(olist);
		return "/shoppingSystem/checkOutPage";
	}
	
	
//
//	@GetMapping(value="", produces = { "application/json; charset=UTF-8" })
//	public @ResponseBody Map<String,Object>  sendCheckoutProduct (){
//		Map<String,Object> map = new HashMap<String,Object>();
//		
//		return map;
//	}
	
	
	
}
