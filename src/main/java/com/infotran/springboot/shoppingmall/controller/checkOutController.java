package com.infotran.springboot.shoppingmall.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.ecpayOperator.EcpayFunction;

@Controller
public class checkOutController {
	
	@Autowired
	ShoppingMallService shopservice;
	
	@Autowired
	OrderListService orlistservice;
	
	@Autowired
	UserSysService userService;
	
	
	@Autowired
	ServletContext context;
	
	File jsonfolder = null; 
	
	String jsonFileDirectory = "D:\\_SpringBoot\\workspace\\PepperNoodles\\src\\main\\resources\\static\\data";
	
	private String confurl = "/shoppingSystem/confirmOrderAndInvoice";
	
	
	private String formfurl = "/shoppingSystem/OrderFormECpay";
	
	
	public checkOutController() {
		jsonfolder = new File(jsonFileDirectory);
		if ( !jsonfolder.exists() )
			jsonfolder.mkdirs();
	}
	
	private static String createNewDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String format = formatter.format(date);
		return format;
	}

	@PostMapping(value="/checkoutURL")
	public @ResponseBody String urlToCheckOut(){
		return "/shoppingSystem/checkOutPage";
	}
	
	@PostMapping(value="/cheqInvoicecheckOutController", produces = { "application/json; charset=UTF-8","application/x-www-form-urlencoded" })
	public @ResponseBody Map<String,Object> cheqOutToInvoiceAndRecieveAddress(
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
		String prostr = "";
		//商品遍歷
		for (int i=0;i<len;i++) {
			int amount = amountlist.get(i);
			int id = idlist.get(i);
			odetail = new OrderDetail();
			Product product = shopservice.findById(id);
			//要更新Product 數量
			product.setQuantity(product.getQuantity()-amount);
			shopservice.save(product);
			//
			odetail.setProduct(product);
			odetail.setAmount(amount);
			odset.add(odetail);
			odetail.setOrderlist(olist);
			prostr += product.getProductName() + "x" + amount + "#" ;
		}
		snowfFlakeUUID uuidgen = new snowfFlakeUUID();
		long uuid = uuidgen.nextId();
		olist.setReceiveAddress(rmap.get("address"));
		olist.setReceiveName(rmap.get("reciever"));
		olist.setReceivePhone(rmap.get("rphone"));
		olist.setUuid(uuid);
		olist.setStatus("尚未付款");
		olist.setOdetails(odset);
		//
		Integer Totalcost = 0 ;
		Set <Product> pset = new HashSet<Product>();
		for (int j = 0 ; j < len ;j++ ) {
			Product product = shopservice.findById(idlist.get(j));
			Totalcost += product.getProductPrice()*amountlist.get(j);
			pset.add(shopservice.findById(idlist.get(j)));
		}
		olist.setTotalCost(Totalcost);
		orlistservice.save(olist);
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.writeValue(new File(jsonFileDirectory,"Order"+uuid), pset);
		OrderList getOrderIdByUUID = orlistservice.findOrderListByUUID(uuid);
		String orderidstr =String.valueOf(getOrderIdByUUID.getOrderId());
		AllInOne allinone = new AllInOne("");
		AioCheckOutALL aiocheckoutall = new AioCheckOutALL();//物件class
		String tradeNo = "PPNOrder" + orderidstr ;
		aiocheckoutall.setMerchantTradeNo(tradeNo);
		String format = createNewDate();
		aiocheckoutall.setMerchantTradeDate(format);
		aiocheckoutall.setTotalAmount(String.valueOf(Totalcost));
		aiocheckoutall.setTradeDesc("PPN商城購物");
		aiocheckoutall.setItemName(prostr);
		aiocheckoutall.setReturnURL("https://f09741b1dd9d.ngrok.io/PepperNoodles/NewFile");
		aiocheckoutall.setOrderResultURL("http://localhost:433/PepperNoodles/shoppingSystem/confirmOrderAndInvoice");
//		String CheckMacValue = EcpayFunction.genCheckMacValue("5294y06JbISpM5x9 ", "v77hoKGq4kWxNNIS", aiocheckoutall);
		String out = allinone.aioCheckOut(aiocheckoutall, null);
		Map<String,Object> cheqOutMap = new HashMap<String,Object>();
		cheqOutMap.put("url", formfurl);
		cheqOutMap.put("ecpayform", out);
//		cheqOutMap.put("uuid", uuid);
//		System.out.println("CheckMacValue==>>>>>>"+CheckMacValue);
		context.setAttribute("aiocheckoutall", aiocheckoutall);
		
		return cheqOutMap;
	}
	
	
	@PostMapping(value="/NewFile")
	public @ResponseBody String getResultFromCreditCardPage() {
		System.out.println("=================<<<<打回來了>>>>===================");
		AioCheckOutALL aiocheckoutall = (AioCheckOutALL)context.getAttribute("aiocheckoutall");
		String orderidstr = aiocheckoutall.getMerchantTradeNo().substring(8);
		System.out.println("訂單編號"+orderidstr);
		OrderList orlist = orlistservice.findById(Integer.parseInt(orderidstr));
		System.out.println("抓id出來的====>>>>>"+orlist.getStatus());
		orlist.setStatus("已付款");
		orlistservice.save(orlist);
		return "ok";
	}

	
	
	
	
	
}
