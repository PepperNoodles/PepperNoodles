package com.infotran.springboot.shoppingmall.controller;

import java.io.File;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.model.Product;
import com.infotran.springboot.shoppingmall.service.OrderListService;
import com.infotran.springboot.shoppingmall.service.ShoppingMallService;
import com.infotran.springboot.shoppingmall.util.snowfFlakeUUID;
import com.infotran.springboot.userAccsystem.service.UserSysService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class checkOutController {
	
	@Autowired
	ShoppingMallService shopservice;
	
	@Autowired
	OrderListService orlistservice;
	
	@Autowired
	UserSysService userService;
	
	File jsonfolder = null; 
	
	String jsonFileDirectory = "D:\\_SpringBoot\\workspace\\PepperNoodles\\src\\main\\resources\\static\\data";
	
	public checkOutController() {
		jsonfolder = new File(jsonFileDirectory);
		if ( !jsonfolder.exists() )
			jsonfolder.mkdirs();
	}

	@PostMapping(value="/checkoutURL")
	public @ResponseBody String urlToCheckOut(){
		return "/shoppingSystem/checkOutPage";
	}
	
	@PostMapping(value="/cheqInvoicecheckOutController")
	public @ResponseBody String cheqOutToInvoiceAndRecieveAddress(
			@RequestPart("idlist")String toId,
			@RequestPart("amountlist")String toAmount,
			@RequestPart("robject")String robject,
			HttpServletRequest request) throws Exception {
		ArrayList<Integer> idlist =  new ObjectMapper().readValue
				(toId, new TypeReference<ArrayList<Integer>>() {});
		ArrayList<Integer> amountlist =  new ObjectMapper().readValue
				(toAmount, new TypeReference<ArrayList<Integer>>() {});
		Map<String,String> rmap =  new ObjectMapper().readValue
				(robject, new TypeReference<HashMap<String,String>>() {});
		UserAccount user = (UserAccount) request.getSession().getAttribute("userAccount");
		UserAccount accessuser = userService.findByAccountIndex(user.getAccountIndex());
		Integer len = idlist.size();
		OrderDetail odetail = null;
		OrderList olist = new OrderList();
		olist.setUser(accessuser);
		Set <OrderDetail> odset = new LinkedHashSet<OrderDetail>();
		for (int i=0;i<len;i++) {
			odetail = new OrderDetail();
			Product product = shopservice.findById(idlist.get(i));
			odetail.setProduct(product);
			odetail.setAmount(amountlist.get(i));
			odset.add(odetail);
			odetail.setOrderlist(olist);
		}
		snowfFlakeUUID uuidgen = new snowfFlakeUUID();
		long uuid = uuidgen.nextId();
		olist.setReceiveAddress(rmap.get("address"));
		olist.setReceiveName(rmap.get("reciever"));
		olist.setReceivePhone(rmap.get("rphone"));
		olist.setUuid(uuid);
		olist.setOdetails(odset);
		//
		Integer Totalcost = 0 ;
		Set <Product> pset = new HashSet<Product>();
		for (int j = 0 ; j < len ;j++ ) {
			Product product = shopservice.findById(idlist.get(j));
			Totalcost += product.getProductPrice()*amountlist.get(j);
			pset.add(shopservice.findById(idlist.get(j)));
		}
		System.out.println(Totalcost);
		olist.setTotalCost(Totalcost);
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.writeValue(new File(jsonFileDirectory,"Order"+uuid), pset);
		//
		orlistservice.save(olist);
		return "/shoppingSystem/confirmOrderAndInvoice";
	}
	
	
	
	
	
}
