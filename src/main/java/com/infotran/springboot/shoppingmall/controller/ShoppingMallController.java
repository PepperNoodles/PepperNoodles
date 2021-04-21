package com.infotran.springboot.shoppingmall.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.infotran.springboot.shoppingmall.model.Product;
import com.infotran.springboot.shoppingmall.service.ShoppingMallService;

@RestController
public class ShoppingMallController {

	@Autowired
	ShoppingMallService shopservice;
	
	@Autowired
	Product product;
	
	@Autowired
	ServletContext ctx;
	
	@GetMapping(value="/tagproducts", produces = { "application/json; charset=UTF-8" })
	public List<Product> getAllProductsWithFoodtagsByClickOnShoppingMallBtn(){
		List<Product> productList = shopservice.getPagedProductsByTag("chris@gmail.com", 0, 4);
		return productList;
	}
	
	@GetMapping(value="/seemoretagproducts", produces = { "application/json; charset=UTF-8" })
	public Map<String,Object> seemoreProductsWithFoodtagsByClickOnSeeMoreBtn(){
		List<Product> productList = shopservice.getPagedProductsByTag("chris@gmail.com", 0, 6);
		Integer totalpage = shopservice.getBtnFromTag("chris@gmail.com", 0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	
	@GetMapping(value="/getProductImages")
	public ResponseEntity<byte[]> getBookImage(@RequestParam("no") Integer no) {
		ResponseEntity<byte[]> re = null;
		Product product = shopservice.get(no);
//		String filename = product.getProductName();
//		System.out.println(filename);
		Blob blob =  product.getProductImage();
//		String mimeType = ctx.getMimeType(filename);
//		MediaType mediaType = MediaType.valueOf(mimeType);
		HttpHeaders headers = new HttpHeaders();
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = blob.getBinaryStream();
			byte[] b = new byte[81920];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			headers.setContentType(MediaType.IMAGE_JPEG);
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			re = new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
	
	@GetMapping(value="/getallproducts", produces = { "application/json; charset=UTF-8" })
	public List<Product> getAllProductsByClickOnShoppingMallBtn(){
		List<Product> productList = shopservice.getAllProducts( 0, 6);
		return productList;
	}
	
	@GetMapping(value="/seemoreallproducts", produces = { "application/json; charset=UTF-8" })
	public Map<String,Object> getAllProductsByClickOnSeeAllProducts(){
		List<Product> productList = shopservice.getAllProducts( 0, 6);
		Integer totalpage = shopservice.getBtnFromAll(0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	
	//一般主類別
	@GetMapping(value="/mainclass",consumes = {"application/json"},produces ={ "application/json; charset=UTF-8" } )
	public Map<String,Object> getAllProductsByClickOnCoupon(@RequestParam("mainname")String mainname){
		String mainClassName="";
		if("coupon".equals(mainname)) {
			mainClassName = "票券";
		} else if ("ingredient".equals(mainname)) {
			mainClassName = "食材";
		}
		List<Product> productList= shopservice.findByProductByMainClass(mainClassName, 0, 6);
		Integer totalpage =shopservice.getBtnFromMainClass(mainClassName, 0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	
	//子類別
	@GetMapping(value="/detailclass",consumes = {"application/json"},produces ={ "application/json; charset=UTF-8" } )
	public Map<String,Object> getAllProductsByClickOnDetailClass(@RequestParam("detailname")String detailname){
		String detailClassName = "";
		if ("friedchicken".equals(detailname)) {
			detailClassName = "炸雞";
		} else if ("icecream".equals(detailname)) {
			detailClassName = "冰淇淋";
		} else if ("vegfruit".equals(detailname)) {
			detailClassName = "蔬菜水果";
		} else if ("desert".equals(detailname)) {
			detailClassName = "甜點";
		} else if ("steak".equals(detailname)) {
			detailClassName = "牛排";
		} else if ("hotpot".equals(detailname)) {
			detailClassName = "火鍋";
		} else if ("lambstove".equals(detailname)) {
			detailClassName = "羊肉爐";
		}
		List<Product> productList = shopservice.findProductByDetailClass(detailClassName, 0, 6);
		Integer totalpage = shopservice.getBtnFromDetailClass(detailClassName, 0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	//純tag
	@GetMapping(value="/puretags",consumes = {"application/json"},produces ={ "application/json; charset=UTF-8" } )
	public Map<String,Object> getAllProductsByClickOnExactTags(@RequestParam("tagname")String detailname){
		String tagname = "";
		if ("friedchicken".equals(detailname)) {
			tagname = "炸雞";
		} else if ("icecream".equals(detailname)) {
			tagname = "冰淇淋";
		} else if ("salad".equals(detailname)) {
			tagname = "沙拉";
		} else if ("desert".equals(detailname)) {
			tagname = "甜點";
		} else if ("steak".equals(detailname)) {
			tagname = "牛排";
		} else if ("hotpot".equals(detailname)) {
			tagname = "火鍋";
		} else if ("lambstove".equals(detailname)) {
			tagname = "羊肉爐";
		}
		List<Product> productList = shopservice.getProductsByExactTag(tagname, 0, 6);
		Integer totalpage = shopservice.getBtnFromExactTag(tagname, 0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	//search
	@GetMapping(value="/search",consumes = {"application/json"},produces ={ "application/json; charset=UTF-8" } )
	public Map<String,Object> searchProducts(@RequestParam("input")String input){
		List<Product> productList = shopservice.getProductsBySearch(input, 0, 6);
		Integer totalpage = shopservice.getBtnFromSearch(input, 0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	
	//pricerange的click事件
	@GetMapping("getpricerange/{price}/{flag}")
	public Map<String,Object> getProductsByClickOnPriceRange(@PathVariable(value="price")Integer price,@PathVariable(value="flag")Integer flag,@RequestParam(value="input",required = false)String input){
		List<Product> productList = null ;
		Map<String,Object> map = new HashMap<String,Object>();
		Integer startPrice = price;
		Integer endPrice = price + 500;
		Integer totalpages = 0;
		if (1==flag) {//tag
			productList = shopservice.getPagedProductsByTagAndPriceRange("chris@gmail.com", startPrice, endPrice, 0, 6);
			totalpages = shopservice.getBtnFromTagAndPriceRange("chris@gmail.com", startPrice, endPrice, 0, 6).get("TotalPages");
			map.put("productlist", productList);
			map.put("totalpage",totalpages);
			} else if (2==flag) {//all
				productList = shopservice.findProductByPriceBetween(startPrice, endPrice,0,6);
				totalpages = shopservice.getBtnFromAllAndPriceBetween(startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (3==flag) {//mainclass:3:coupon
				productList = shopservice.findByProductByMainClassAndPriceRange("票券", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromMainClassAndPriceRange("票券", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (4==flag) {//mainclass:3:coupon
				productList = shopservice.findByProductByMainClassAndPriceRange("食材", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromMainClassAndPriceRange("食材", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (5==flag) {
				productList =shopservice.findProductByDetailClassAndPriceRange("炸雞", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromDetailClassAndPriceRange("炸雞", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (6==flag) {
				productList =shopservice.findProductByDetailClassAndPriceRange("冰淇淋", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromDetailClassAndPriceRange("冰淇淋", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (7==flag) {
				productList =shopservice.findProductByDetailClassAndPriceRange("蔬菜水果", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromDetailClassAndPriceRange("蔬菜水果", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (8==flag) {
				productList =shopservice.findProductByDetailClassAndPriceRange("甜點", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromDetailClassAndPriceRange("甜點", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (9==flag) {
				productList =shopservice.findProductByDetailClassAndPriceRange("牛排", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromDetailClassAndPriceRange("牛排", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (10==flag) {
				productList =shopservice.findProductByDetailClassAndPriceRange("火鍋", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromDetailClassAndPriceRange("火鍋", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (11==flag) {
				productList =shopservice.findProductByDetailClassAndPriceRange("羊肉爐", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromDetailClassAndPriceRange("羊肉爐", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (12==flag) {//puretag
				productList =shopservice.getProductsByExactTagAndPriceRange("炸雞", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromExactTagAndPriceRange("炸雞", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (13==flag) {//puretag
				productList =shopservice.getProductsByExactTagAndPriceRange("冰淇淋", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromExactTagAndPriceRange("冰淇淋", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (14==flag) {//puretag
				productList =shopservice.getProductsByExactTagAndPriceRange("沙拉", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromExactTagAndPriceRange("沙拉", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (15==flag) {//puretag
				productList =shopservice.getProductsByExactTagAndPriceRange("甜點", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromExactTagAndPriceRange("甜點", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (16==flag) {//puretag
				productList =shopservice.getProductsByExactTagAndPriceRange("火鍋", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromExactTagAndPriceRange("火鍋", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} else if (1001==flag) {//search
				productList =shopservice.getProductsBySearchAndPriceRange(input, startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromSearchAndPriceRange(input, startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
			} 
		return map; 
	}
	
	
	//分頁click事件(ifprice==1是價格區間)
	@GetMapping("/getpage/{ifprice}/{flag}/{page}")
	public List<Product> getAllProductsByClickOnPage(@PathVariable(value="ifprice")Integer ifprice,@PathVariable(value="flag")Integer flag,@PathVariable("page") Integer page,@RequestParam(value="input",required = false)String input){
		List<Product> productList = null ; 
		Integer startPrice =0;
		Integer endPrice = 0;
		if (ifprice>0) {
			startPrice = ifprice;
			endPrice = ifprice + 500;
		}
		if (0==ifprice) {//非 價格區間
			if (1==flag) {//see more tag products
				productList = shopservice.getPagedProductsByTag("chris@gmail.com", page, 6);
				System.out.println("1");
			} else if (2==flag) {//see more all products
				productList = shopservice.getAllProducts(page, 6);
				System.out.println("2");
			} else if (3==flag) {//main class coupon
				productList = shopservice.findByProductByMainClass("票券",page, 6);
			} else if (4==flag) {//main class coupon
				productList = shopservice.findByProductByMainClass("食材",page, 6);
			} else if (5==flag) {//detail class fried chick
				productList = shopservice.findProductByDetailClass("炸雞", page, 6);
			} else if (6==flag) {//detail class fried chick
				productList = shopservice.findProductByDetailClass("冰淇淋", page, 6);
			} else if (7==flag) {//detail class fried chick
				productList = shopservice.findProductByDetailClass("蔬菜水果", page, 6);
			} else if (8==flag) {//detail class fried chick
				productList = shopservice.findProductByDetailClass("甜點", page, 6);
			} else if (9==flag) {//detail class fried chick
				productList = shopservice.findProductByDetailClass("牛排", page, 6);
			} else if (10==flag) {//detail class fried chick
				productList = shopservice.findProductByDetailClass("火鍋", page, 6);
			} else if (11==flag) {//detail class fried chick
				productList = shopservice.findProductByDetailClass("羊肉爐", page, 6);
			} else if (12==flag) {//pure tag
				productList = shopservice.getProductsByExactTag("炸雞", page, 6);
			} else if (13==flag) {//pure tag
				productList = shopservice.getProductsByExactTag("冰淇淋", page, 6);
			} else if (14==flag) {//pure tag
				productList = shopservice.getProductsByExactTag("沙拉", page, 6);
			} else if (15==flag) {//pure tag
				productList = shopservice.getProductsByExactTag("甜點", page, 6);
			} else if (16==flag) {//pure tag
				productList = shopservice.getProductsByExactTag("火鍋", page, 6);
			} else if (1001==flag) {
				productList = shopservice.getProductsBySearch(input,page,6);
			}
		}else { 
			if (1==flag) {
				productList = shopservice.getPagedProductsByTagAndPriceRange("chris@gmail.com", startPrice, endPrice, page, 6);
			} else if (2==flag) {
				productList = shopservice.findProductByPriceBetween(startPrice, endPrice, page, 6);
			} else if (3==flag) {
				productList = shopservice.findByProductByMainClassAndPriceRange("票券", startPrice, endPrice, page, 0);
			} else if (4==flag) {
				productList = shopservice.findByProductByMainClassAndPriceRange("食材", startPrice, endPrice, page, 0);
			} else if (5==flag) {
				productList = shopservice.findProductByDetailClassAndPriceRange("炸雞", startPrice, endPrice, page, 6);
			} else if (6==flag) {
				productList = shopservice.findProductByDetailClassAndPriceRange("冰淇淋", startPrice, endPrice, page, 6);
			} else if (7==flag) {
				productList = shopservice.findProductByDetailClassAndPriceRange("蔬菜水果", startPrice, endPrice, page, 6);
			} else if (8==flag) {
				productList = shopservice.findProductByDetailClassAndPriceRange("甜點", startPrice, endPrice, page, 6);
			} else if (9==flag) {
				productList = shopservice.findProductByDetailClassAndPriceRange("牛排", startPrice, endPrice, page, 6);
			} else if (10==flag) {
				productList = shopservice.findProductByDetailClassAndPriceRange("火鍋", startPrice, endPrice, page, 6);
			} else if (11==flag) {
				productList = shopservice.findProductByDetailClassAndPriceRange("羊肉爐", startPrice, endPrice, page, 6);
			} else if (12==flag) {//pure tag
				productList = shopservice.getProductsByExactTagAndPriceRange("炸雞", startPrice, endPrice, page, 6);
			} else if (13==flag) {//pure tag
				productList = shopservice.getProductsByExactTagAndPriceRange("冰淇淋", startPrice, endPrice, page, 6);
			} else if (14==flag) {//pure tag
				productList = shopservice.getProductsByExactTagAndPriceRange("沙拉", startPrice, endPrice, page, 6);
			} else if (15==flag) {//pure tag
				productList = shopservice.getProductsByExactTagAndPriceRange("甜點", startPrice, endPrice, page, 6);
			} else if (16==flag) {//pure tag
				productList = shopservice.getProductsByExactTagAndPriceRange("火鍋", startPrice, endPrice, page, 6);
			} else if (1001==flag) {//search
				shopservice.getProductsBySearchAndPriceRange(input, startPrice, endPrice, page, 6);
			}
		}
		return productList;
	}
	
	

}
