package com.infotran.springboot.shoppingmall.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.shoppingmall.model.Product;
import com.infotran.springboot.shoppingmall.service.OrderListService;
import com.infotran.springboot.shoppingmall.service.ShoppingMallService;

@RestController
public class ShoppingMallController {

	@Autowired
	ShoppingMallService shopservice;
	
	@Autowired
	OrderListService orlistservice;
	
	@Autowired
	Product product;
	
	@Autowired
	ServletContext ctx;
	
	@GetMapping(value="/tagproducts", produces = { "application/json; charset=UTF-8" })
	public List<Product> getAllProductsWithFoodtagsByClickOnShoppingMallBtn(
			HttpServletRequest request) {
		UserAccount user = (UserAccount) request.getSession().getAttribute("userAccount");
		System.out.println(user.getAccountIndex());
		List<Product> productList = shopservice.getPagedProductsByTag(user.getAccountIndex(), 0, 4);
		return productList;
	}
	
	@GetMapping(value="/seemoretagproducts", produces = { "application/json; charset=UTF-8" })
	public Map<String,Object> seemoreProductsWithFoodtagsByClickOnSeeMoreBtn(
			@ModelAttribute("userAccount")UserAccount useraccount,
			HttpServletRequest request){
		UserAccount user = (UserAccount) request.getSession().getAttribute("userAccount");
		List<Product> productList = shopservice.getPagedProductsByTag(user.getAccountIndex(), 0, 6);
		Integer totalpage = shopservice.getBtnFromTag(user.getAccountIndex(), 0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	
	@GetMapping(value={"/getProductImages","/user/rearStage/getProductImages"})
	public ResponseEntity<byte[]> getBookImage(@RequestParam("no") Integer no) {
		ResponseEntity<byte[]> re = null;
		Product product = shopservice.get(no);
		Blob blob =  product.getProductImage();
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
		System.out.println(mainname);
		List<Product> productList= shopservice.findByProductByMainClass(mainname, 0, 6);
		Integer totalpage =shopservice.getBtnFromMainClass(mainname, 0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	
	//子類別
	@GetMapping(value="/detailclass",consumes = {"application/json"},produces ={ "application/json; charset=UTF-8" } )
	public Map<String,Object> getAllProductsByClickOnDetailClass(@RequestParam("detailname")String detailname){
//		String detailClassName = translationForDetailClass(detailname);
		List<Product> productList = shopservice.findProductByDetailClass(detailname, 0, 6);
		Integer totalpage = shopservice.getBtnFromDetailClass(detailname, 0, 6).get("TotalPages");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productlist", productList);
		map.put("totalpage", totalpage);
		return map;
	}
	//純tag
	@GetMapping(value="/puretags",consumes = {"application/json"},produces ={ "application/json; charset=UTF-8" } )
	public Map<String,Object> getAllProductsByClickOnExactTags(@RequestParam("tagname")String tagname){
		String tags;
		if ("冰淇淋標籤".equals(tagname)) {
			tags = tagname.substring(0, 3);
		}else {
			tags = tagname.substring(0, 2);
		}
		System.out.println(tags);
		List<Product> productList = shopservice.getProductsByExactTag(tags, 0, 6);
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
	public Map<String,Object> getProductsByClickOnPriceRange(
			@PathVariable(value="price")Integer price,
			@PathVariable(value="flag")String flag,
			@RequestParam(value="input",required = false)String input){
		List<Product> productList = null ;
		Map<String,Object> map = new HashMap<String,Object>();
		Integer startPrice = price;
		Integer endPrice = price + 500;
		Integer totalpages = 0;
		String flagname ;
		switch (flag) {
			case "1":
				productList = shopservice.getPagedProductsByTagAndPriceRange("chris@gmail.com", startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromTagAndPriceRange("chris@gmail.com", startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
				break;
			case "2":
				productList = shopservice.findProductByPriceBetween(startPrice, endPrice,0,6);
				totalpages = shopservice.getBtnFromAllAndPriceBetween(startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
				break;
			case "票券":
			case "食材":
				productList = shopservice.findByProductByMainClassAndPriceRange(flag, startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromMainClassAndPriceRange(flag, startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
				break;
			case "炸雞":
			case "冰淇淋":
			case "蔬菜水果":
			case "甜點":
			case "牛排":
			case "火鍋":
			case "羊肉爐":
				productList =shopservice.findProductByDetailClassAndPriceRange(flag, startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromDetailClassAndPriceRange(flag, startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
				break;
			case "冰淇淋標籤":
				flagname = flag.substring(0, 3);
				productList =shopservice.getProductsByExactTagAndPriceRange(flagname, startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromExactTagAndPriceRange(flagname, startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
				break;
			case "炸雞標籤":
			case "沙拉標籤":
			case "甜點標籤":
			case "火鍋標籤":
				flagname = flag.substring(0, 2);
				System.out.println(flagname);
				productList =shopservice.getProductsByExactTagAndPriceRange(flagname, startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromExactTagAndPriceRange(flagname, startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
				break;
			case "1001":
				productList =shopservice.getProductsBySearchAndPriceRange(input, startPrice, endPrice, 0, 6);
				totalpages = shopservice.getBtnFromSearchAndPriceRange(input, startPrice, endPrice, 0, 6).get("TotalPages");
				map.put("productlist", productList);
				map.put("totalpage",totalpages);
				break;
			default:
				break;
		}
		return map; 
	}
	
	
	//分頁click事件(ifprice==1是價格區間)
	@GetMapping("/getpage/{ifprice}/{flag}/{page}")
	public List<Product> getAllProductsByClickOnPage(
			@PathVariable(value="ifprice")Integer ifprice,
			@PathVariable(value="flag")String flag,
			@PathVariable("page") Integer page,
			@RequestParam(value="input",required = false)String input){
		List<Product> productList = null ; 
		Integer startPrice =0;
		Integer endPrice = 0;
		String flagname;
		if (ifprice!=5) {
			startPrice = ifprice;
			endPrice = ifprice + 500;
		}
		if (5==ifprice) {//非 價格區間
			switch (flag) {
				case "1":
					productList = shopservice.getPagedProductsByTag("chris@gmail.com", page, 6);
					break;
				case "2":
					productList = shopservice.getAllProducts(page, 6);
					break;
				case "票券":
				case "食材":
					productList = shopservice.findByProductByMainClass(flag,page, 6);
					break;
				case "炸雞":
				case "冰淇淋":
				case "蔬菜水果":
				case "甜點":
				case "牛排":
				case "火鍋":
				case "羊肉爐":
					productList = shopservice.findProductByDetailClass(flag, page, 6);
					break;
				case "炸雞標籤":
				case "沙拉標籤":
				case "甜點標籤":
				case "火鍋標籤":
					flagname = flag.substring(0, 2);
					productList = shopservice.getProductsByExactTag(flagname, page, 6);
					break;
				case "冰淇淋標籤":
					flagname = flag.substring(0, 3);
					productList = shopservice.getProductsByExactTag(flagname, page, 6);
					break;
				case "1001":
					productList = shopservice.getProductsBySearch(input,page,6);
					break;
				default:
					break;
				}
		}else { 
			switch (flag) {
				case "1":
					productList = shopservice.getPagedProductsByTagAndPriceRange("chris@gmail.com", startPrice, endPrice, page, 6);
					break;
				case "2":
					productList = shopservice.findProductByPriceBetween(startPrice, endPrice, page, 6);
					break;
				case "票券":
				case "食材":
					productList = shopservice.findByProductByMainClassAndPriceRange(flag, startPrice, endPrice, page, 6);
					break;
				case "炸雞":
				case "冰淇淋":
				case "蔬菜水果":
				case "甜點":
				case "牛排":
				case "火鍋":
				case "羊肉爐":
					productList = shopservice.findProductByDetailClassAndPriceRange(flag, startPrice, endPrice, page, 6);
					break;
				case "炸雞標籤":
				case "沙拉標籤":
				case "甜點標籤":
				case "火鍋標籤":
					flagname = flag.substring(0, 2);
					productList = shopservice.getProductsByExactTagAndPriceRange(flagname, startPrice, endPrice, page, 6);
					break;
				case "冰淇淋標籤":
					flagname = flag.substring(0, 3);
					productList = shopservice.getProductsByExactTagAndPriceRange(flagname, startPrice, endPrice, page, 6);
					break;
				case "1001":
					shopservice.getProductsBySearchAndPriceRange(input, startPrice, endPrice, page, 6);
					break;
				default:
					break;
			}
		}
		return productList;
	}
	
	@GetMapping(value="/informUserProductStatus")
	public Map<String,List<Product>> informUserProductStatus() {
		Calendar calendar = new GregorianCalendar(); 
		Date now = new Date();
		calendar.setTime(now);
		Date todayDate = calendar.getTime();
		calendar.add(Calendar.DATE, -3);
		Date startdate = calendar.getTime();
		SimpleDateFormat datestr = new SimpleDateFormat("yyyy/MM/dd");
		String sDate = datestr.format(startdate);
		String tDate = datestr.format(todayDate);
		List<Product> pofflist = shopservice.findUpdatedProductByDate(sDate);
		List<Product> pnewlist = shopservice.findNewProductByReleasedDate(tDate);
		Map<String,List<Product>> pmap = new HashMap<>();
		pmap.put("offlist", pofflist);
		pmap.put("newlist", pnewlist);
		return pmap;
	}
	
	
}
