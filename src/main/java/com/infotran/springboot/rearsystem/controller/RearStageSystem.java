package com.infotran.springboot.rearsystem.controller;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Blob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

import org.aspectj.apache.bcel.classfile.Module.Require;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.model.Product;
import com.infotran.springboot.shoppingmall.model.ProductDetailClass;
import com.infotran.springboot.shoppingmall.service.OrderListService;
import com.infotran.springboot.shoppingmall.service.ProductDetailClassService;
import com.infotran.springboot.shoppingmall.service.ShoppingMallService;
import com.infotran.springboot.shoppingmall.util.ProductList;

@Controller
public class RearStageSystem {

	String imageRootDirectory = "D:\\_SpringBoot\\image";
	
	File imageFolder = null; 
	
	@Autowired
	ShoppingMallService shoppingmallservice;
	
	@Autowired
	ProductDetailClassService detailservice;
	
	@Autowired
	OrderListService olistservice;
	
//	@Autowired
//	Product product;
	

	private static Date getFirstMonthDay(Integer year,Integer month) {
		YearMonth yearMonth = YearMonth.of( year, month );  // January of 2015.
		LocalDate firstOfMonth = yearMonth.atDay( 1 );
		Date first = Date.from(firstOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return first;
	}
	private static Date getLastMonthDay(Integer year,Integer month) {
		YearMonth yearMonth = YearMonth.of( year, month ); 
		LocalDate last = yearMonth.atEndOfMonth();
		Date lastdate = Date.from(last.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return lastdate;
	}
	
	private static boolean sameDate(Date d1, Date d2){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		fmt.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
		return fmt.format(d1).equals(fmt.format(d2));
	}
	
	
	
	@PostMapping(value="/insproduct")
	public @ResponseBody Map<String,String> Registure( @RequestPart("productinfo")String toString,@RequestPart("file")MultipartFile file,HttpServletResponse response) throws Exception{
		Map<String, String> map = new HashMap<>();
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		Product product = new Product();
		//找detailid
		Integer detailid = Integer.parseInt(dispatch.get("productDetailClass"));
		ProductDetailClass productdetailclass = detailservice.findById(detailid);
		//
		product.setProductName(dispatch.get("productName"));
		product.setProductPrice(Integer.parseInt(dispatch.get("productPrice")));
		product.setDescription(dispatch.get("description"));
		product.setQuantity(Integer.parseInt(dispatch.get("quantity")));
		product.setProductDetailClass(productdetailclass);
		product.setStatus(dispatch.get("status"));
		String originalFilename = file.getOriginalFilename();
		String extFilename = ""; 
		if (originalFilename.length() > 0 && originalFilename.lastIndexOf(".") > -1) {
			extFilename = originalFilename.substring(originalFilename.lastIndexOf("."));
			System.out.println(extFilename);
		}
		if (file != null && !file.isEmpty()) {
			try {
				byte[] b = file.getBytes();
				Blob blob = new SerialBlob(b);
				product.setProductImage(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		try {
			Product p = shoppingmallservice.save(product);
			if(p != null) {
				map.put("success", "新增成功");
				response.setStatus(HttpServletResponse.SC_CREATED);
			}else {
				map.put("fail", "新增失敗");
			}
		}catch (Exception e) {
			map.put("fail2", e.getMessage());
		}
		
		try {
//			File fileplace = new File(imageFolder, "UserImage_" + product.getProductName() + extFilename);
//			file.transferTo(fileplace);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
		}
		return map;
	}
	
	
	@GetMapping(value="/getproductlist")
	public @ResponseBody Map<String,ArrayList<ProductList>> getProductList(){
		Map<String, ArrayList<ProductList>> pmap = new HashMap<>();
		ArrayList<ProductList> parray = new ArrayList<>();
		ProductList palist =  null;
		List<Product> plist = shoppingmallservice.findAll();
		for (Product p : plist) {
		   palist = new ProductList();
		   palist.setProductId(p.getProductId());
		   palist.setProductName(p.getProductName());
		   palist.setProductPrice(p.getProductPrice());
		   palist.setQuantity(p.getQuantity());
		   String detailclassname = p.getProductDetailClass().getDetailClassName();
		   palist.setProductDetailClassName(detailclassname);
		   palist.setRealeasedDate(p.getRealeasedDate());
		   palist.setStatus(p.getStatus());
		   Integer amount = olistservice.countSumByProductId(p.getProductId());
		   if (amount == null)amount=0;
		   palist.setSalesamount(amount);
		   parray.add(palist);
		}
		pmap.put("productlist", parray);
		return pmap;
	}
	
	@GetMapping(value="/user/rearStage/openProductToEdit")
	public @ResponseBody Map<String,Object> openProductToEdit (@RequestParam("id")Integer id){
		Map<String, Object> openmap = new HashMap<>();
		Product product = shoppingmallservice.findById(id);
		openmap.put("productToEdit", product);
		return openmap;
	}
	
	@PostMapping(value="/editproduct",consumes={"multipart/form-data"})
	public @ResponseBody String editProduct( @RequestPart("productinfo")String toString,
			@RequestPart("file")MultipartFile file) throws Exception {
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		Product product = shoppingmallservice.get(Integer.valueOf(dispatch.get("productid")));
		product.setProductName(dispatch.get("productName"));
		product.setProductPrice(Integer.valueOf(dispatch.get("productPrice")));
		product.setDescription(dispatch.get("description"));
		product.setQuantity(Integer.valueOf(dispatch.get("quantity")));
		Integer detailid = Integer.parseInt(dispatch.get("productDetailClass"));
		ProductDetailClass productdetailclass = detailservice.findById(detailid);
		product.setProductDetailClass(productdetailclass);
		product.setStatus(dispatch.get("status"));
		if (file != null && !file.isEmpty()) {
			try {
				byte[] b = file.getBytes();
				Blob blob = new SerialBlob(b);
				product.setProductImage(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		shoppingmallservice.save(product);
		return "ok";
	}
	
//	@GetMapping(value="getoptionproductname")
//	public @ResponseBody List<Product> getProductNameForOption(){
//		return shoppingmallservice.findAll();
//	}
	
	
	@SuppressWarnings("unused")
	@GetMapping(value="/saleschart")
	public @ResponseBody Map<String,Object> gensaleschart(
				@RequestParam("year") Integer year,
				@RequestParam("month") Integer month,
				@RequestParam(value="pid",required=false)Integer productid){
		System.out.println("hi");
		Date first = getFirstMonthDay(year,month);
		Date lastdate = getLastMonthDay(year,month);
		ArrayList<OrderList> olist = olistservice.findOrderListByDateBetween(first, lastdate);
		ArrayList<Double> salesamount = new ArrayList<>();
		SimpleDateFormat datestr = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat datestrint = new SimpleDateFormat("yyyyMMdd");
		Integer lastday = Integer.valueOf(datestrint.format(lastdate).substring(6));
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(first);
		for (int i =1 ; i<= lastday;i++) {
			Date dateiterate =calendar.getTime();
			double d1 = 0.00 ;
			if (productid!=0) {//單商品銷售
				Integer prototal;
				for (OrderList ol : olist) {
					if (sameDate(dateiterate,ol.getOrderCreatedDate())) {
						 ArrayList<OrderDetail> orderdetaillist = olistservice.findOrderDetailByFkOrderId(ol.getOrderId());
						 for (OrderDetail od : orderdetaillist) {
							 if (od.getProduct().getProductId()==productid) {
								 prototal = od.getProduct().getProductPrice() * od.getAmount();
								 System.out.println("單一商品價格:"+prototal);
								 Double db  = prototal/10000.0;
								 DecimalFormat df = new DecimalFormat("##.00");
								 d1 = Double.parseDouble(df.format(db));
							 }
						 }
					}else {
						d1 = 0.00;
					}
				}
			}else {
				//總銷售
			    Integer total = olistservice.sumBySameDateWithConvertTo111(datestr.format(dateiterate));
			    if (total!=null) {
			    	 Double db = total/10000.0;
			    	 DecimalFormat df = new DecimalFormat("##.00");
			    	 d1 = Double.parseDouble(df.format(db));
			    }else {
			    	d1 = 0.00;
			    }
			}
		    calendar.add(Calendar.DATE,1);
		    salesamount.add(d1);
		}
		Map<String, Object> salesmap = new HashMap<>();
		salesmap.put("salesamount", salesamount);
		return salesmap;
	}
	
	@GetMapping(value="/deleteProducts")
	public @ResponseBody String deleteProductId(@RequestParam("deleteid")Integer productid) {
		Product product = shoppingmallservice.get(productid);
		product.setProductDetailClass(null);
		shoppingmallservice.delete(product);
		return "ok";
	}
	
	
}
