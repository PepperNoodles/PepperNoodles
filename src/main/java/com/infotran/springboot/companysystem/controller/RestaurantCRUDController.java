package com.infotran.springboot.companysystem.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.infotran.springboot.commonmodel.CompanyDetail;
import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantBusinHour;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.companysystem.service.impl.BusinessHourServiceImpl;
import com.infotran.springboot.companysystem.service.impl.CompanyDetailServiceImpl;
import com.infotran.springboot.companysystem.validator.RestaurantValidator;
import com.infotran.springboot.userAccsystem.service.UserSysService;

@Controller
@SessionAttributes(names = {"updateRestaurant","foodTagListLength"} )
public class RestaurantCRUDController {
	
//	若@GetMapping("/restpicture/{id}")找不到圖就用此圖
	String noImage = "/images/NoImage/restaurantdefault.png";
	
	@Autowired
	ServletContext context;

	@Autowired
	RestaurantService restaurantService; 

	@Autowired
	RestaurantValidator restvalidator;
	
	@Autowired
	UserSysService userSysService;
	
	@Autowired
	CompanyDetailServiceImpl companyDetailServiceImpl;
	
	@Autowired
	BusinessHourServiceImpl businessHourServiceImpl; 
	
	
	// 顯示所有餐廳資料
	@GetMapping("/showAllrest")
	public String allRest(Model model) {
		model.addAttribute("restaurants", restaurantService.getAllRestaurant());
		return "company/AllRestaurants";
	}
	// 顯示所有餐廳資料ByComId
	@GetMapping("/showAllrestByComId/{comId}")
	public String allRestById(Model model,@PathVariable("comId")Integer comId) {
		
		System.out.println("comId="+comId);
		CompanyDetail companyDetail = companyDetailServiceImpl.findById(comId);
		
		UserAccount userAccount = companyDetail.getUserAccount();
		
		Set<Restaurant> restaurants = userAccount.getRestaurant();
		model.addAttribute("restaurants", restaurants);
		
		return "company/AllRestaurants";
	}
	
	// 顯示餐廳營業時間頁面
	@GetMapping("/Hours/{restIdForSettingHour}")
	public String showRestaurantBusinHourPage(Model model,@PathVariable("restIdForSettingHour") Integer id) {
		RestaurantBusinHour restaurantBusinHour=new RestaurantBusinHour();
		Restaurant restForSettingHour = restaurantService.get(id);
		restaurantBusinHour.setRestaurant(restForSettingHour);
//		System.out.println("restid====="+restaurantBusinHour.getRestaurant().getRestaurantId());
		model.addAttribute("restaurantBusinHour", restaurantBusinHour);
		model.addAttribute("restForSettingHour", restForSettingHour);
		return "company/RestaurantBusinHour";
	}
	
	//ajax get方法純拿餐廳資料
	@GetMapping("/getHours/{restId}")
	public @ResponseBody List<RestaurantBusinHour> getHours(Model model,@PathVariable("restId") Integer id) {
		List<RestaurantBusinHour> businessHourlist = businessHourServiceImpl.businHourSByRestID(id);
		return businessHourlist;
	}
	
	
	//ajax post方法存取餐廳資料
	@PostMapping("/Hours/{restIdForSettingHour}")
	public  @ResponseBody List<RestaurantBusinHour> addRestaurantBusinHour(@ModelAttribute("restaurantBusinHour") RestaurantBusinHour restaurantBusinHour, BindingResult result,
			@PathVariable("restIdForSettingHour") Integer id,Model model) {
		Restaurant restForSettingHour = restaurantService.get(id);
		//設定營業時間與餐廳關聯性
		restaurantBusinHour.setRestaurant(restForSettingHour);
		//設定變數以方便整理前端傳來資料
		Integer fk_restId = restaurantBusinHour.getRestaurant().getRestaurantId();
//		System.out.println("fk_restId="+fk_restId);
		String day = restaurantBusinHour.getDay();
//		System.out.println("前端傳來day="+day);
		
		String openTime1 = restaurantBusinHour.getOpenTime();
		System.out.println("前端傳來openTime1="+openTime1);
		
		String closeTime1 = restaurantBusinHour.getCloseTime();
//		System.out.println("前端傳來closeTime1="+closeTime1);
		
		String openTime2 = restaurantBusinHour.getOpenTime2nd();
//		System.out.println("openTime2="+openTime2);
		
		String closeTime2 = restaurantBusinHour.getCloseTime2nd();
//		System.out.println("closeTime2="+closeTime2);
		
		String openTime3 = restaurantBusinHour.getOpenTime3rd();
//		System.out.println("openTime3="+openTime3);
		
		String closeTime3 = restaurantBusinHour.getCloseTime3rd();
//		System.out.println("closeTime3="+closeTime3);
		
		
		//判斷DB有無此餐廳營業時間 條件 1.day  2.restaurantId
		RestaurantBusinHour restaurantBusinHour2 = businessHourServiceImpl.findByDayAndRestaurantId(day, fk_restId);
		if(restaurantBusinHour2==null) {
			businessHourServiceImpl.save(restaurantBusinHour);
		}
		else {
			restaurantBusinHour2.setOpenTime(openTime1);
			restaurantBusinHour2.setCloseTime(closeTime1);
			
			restaurantBusinHour2.setOpenTime2nd(openTime2);
			restaurantBusinHour2.setCloseTime2nd(closeTime2);
			
			restaurantBusinHour2.setOpenTime3rd(openTime3);
			restaurantBusinHour2.setCloseTime3rd(closeTime3);
			
			businessHourServiceImpl.update(restaurantBusinHour2);
		}
		
	
		List<RestaurantBusinHour> businessHourlist = businessHourServiceImpl.businHourSByRestID(id);
		System.out.println("產生list");


		return businessHourlist;
	}



	
	@ModelAttribute
	public void foodTagListDataTest(Model model) {
		List<FoodTag> foodTagListTest = restaurantService.getAllFoodTag();
		model.addAttribute("foodTagListTest", foodTagListTest);
	}
	// 連進新增餐廳時 給預設值 回傳新增頁
	@GetMapping(value = "/addrest")
	public String initRestaurant(Model model) {
		Restaurant rest= new Restaurant();
		rest.setRestaurantName("幽靈炒飯好吃");
		rest.setRestaurantAddress("台北市 信義路 100號");
		rest.setRestaurantContact("0909053909");
		rest.setRestaurantWebsite("facebook.com");
		model.addAttribute("restaurant", rest);
		return "company/InsertRestaurant";
	}


	// 新增餐廳
	@PostMapping(value = "/addrest")
	public String addrest(@ModelAttribute("restaurant") Restaurant rest, BindingResult result, Model model,	HttpSession session) {
		
		// 先關掉 tag有問題
		// 餐廳validator進行資料檢查  
		restvalidator.validate(rest, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println(".validate()後有錯誤：" + error);
			}
			return "company/InsertRestaurant";
		}
		// 從model取照片
		MultipartFile picture = rest.getProductImage();
		// 建立Blob物件，交由 Hibernate 寫入資料庫
		if (picture != null && !picture.isEmpty()) {
			try {
				byte[] b = picture.getBytes();
				Blob blob = new SerialBlob(b);
				rest.setRestaurantPhoto(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		//綁定account_id
		CompanyDetail comDetail=(CompanyDetail) session.getAttribute("comDetail");
		Integer companyDetailId = comDetail.getCompanyDetailId();
		UserAccount useracc=comDetail.getUserAccount();
		//useracc是detached狀態 必須用userSysService在抓一次新的物件
		String index=useracc.getAccountIndex();
		UserAccount user = userSysService.findByAccountIndex(index);
	
		rest.setUserAccount(user);  
		
		//嘗試印印看我輸入的TAG
		Set<FoodTag> foodTagset=rest.getFoodTag();
		Iterator<FoodTag> it = foodTagset.iterator();
		 while (it.hasNext()) {   
              System.out.println("欲新增餐廳添加標籤:"+it.next().getFoodTagName());}

		restaurantService.save(rest);
		System.out.println("餐廳新增成功");
		


		
		return "redirect:/showAllrestByComId/"+companyDetailId;

	}
	/*更新餐廳↓*/
	// 當使用者需要修改時，本方法送回含有餐廳資料的表單，讓使用者進行修改
	@GetMapping("/updateRest/{restaurantId}")
	public String showRestDataForm(@PathVariable("restaurantId") Integer id, Model model) {
		Restaurant rest = restaurantService.get(id);
		model.addAttribute("updateRestaurant", rest);
		System.out.println("選擇更新編號:" + id + "的餐廳");
		return "company/UpdateRestaurant";
	}

	// 收到更新post後進行檢查 若沒有問題就完成交易 有問題傳回修改頁面
	@PostMapping("/updateRest/{restaurantId}")
	public String modify(@ModelAttribute("updateRestaurant") Restaurant restaurant, BindingResult result, Model model,
			@PathVariable("restaurantId") Integer id,@RequestPart("productImage")MultipartFile productImage,HttpSession session ) {
		// validator檢查錯誤
		restvalidator.validate(restaurant, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			//若有錯 回到修改頁面
			return "company/UpdateRestauran";
		}
		//檢查提交表單的上傳圖片檔
	
		if(productImage!=null&& !productImage.isEmpty()) {
			try {
				byte[] b = productImage.getBytes();
				Blob blob = new SerialBlob(b);
				restaurant.setRestaurantPhoto(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
			restaurantService.update(restaurant);
		}
		else {
			Blob oldRestPhoto = restaurantService.get(restaurant.getRestaurantId()).getRestaurantPhoto();
			restaurant.setRestaurantPhoto(oldRestPhoto);
			restaurantService.update(restaurant);
		}
		CompanyDetail comDetail=(CompanyDetail) session.getAttribute("comDetail");
		Integer companyDetailId = comDetail.getCompanyDetailId();
		return "redirect:/showAllrestByComId/"+companyDetailId;
	}
	/*更新餐廳↑*/
	
	
	// 刪除餐廳
	@DeleteMapping("/deleteRest/{Id}")
	public String deleterest(@PathVariable("Id") Integer id,HttpSession session) {
		
		
		//先去除掉關聯性再刪掉 不然會連同AccountUser+Role的comapny一同刪除== 幹!
		Restaurant restToDelete = restaurantService.get(id);
		restToDelete.setRestaurantBusinHour(null);
		restToDelete.setFoodTag(null);
		restToDelete.setUserAccount(null);
		System.out.println("將編號:" + id + "餐廳的companyID及foodTag設為null---去除關聯性");
		restaurantService.update(restToDelete);
		System.out.println("刪除了編號:" + id + "的餐廳!!");
		restaurantService.delete(id);	
		CompanyDetail comDetail=(CompanyDetail) session.getAttribute("comDetail");
		Integer companyDetailId = comDetail.getCompanyDetailId();
		return "redirect:/showAllrestByComId/"+companyDetailId;
	}
	
	// check地址有無餐廳註冊
	@PostMapping("/checkRAddress")
	public @ResponseBody Map<String,String> checkRAddress(@RequestParam("value") String rAdd) {
		System.out.println("驗證地址:"+rAdd);
		Restaurant rest=restaurantService.findByRestaurantAddress(rAdd);
		Map<String,String> map = new HashMap<String, String>();
		
		try {
			String restName = rest.getRestaurantName();
			System.out.println("驗證餐廳為"+restName);
			map.put("result", "此地址有餐廳註冊 不可新增");
			map.put("checkboolean", "false");
		}
		catch (NullPointerException e) {
			map.put("result", "此地址可以新增");
			map.put("checkboolean", "true");
		}
		System.out.println(map.get("result"));
		return map;
	}
	

	//請求餐廳標籤2
	@GetMapping("/restTag2/{id}")
	public @ResponseBody Set<FoodTag> restTag2(@PathVariable("id") Integer id) {
		Restaurant rest = restaurantService.get(id);
		Set<FoodTag> set = rest.getFoodTag();
		return set;
		
	}
	
	// 給圖用↓
	public byte[] blobToByteArray(Blob blob) {
		byte[] result = null;
		try (InputStream is = blob.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			byte[] b = new byte[819200];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			result = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private byte[] fileToByteArray(String path) {
		byte[] result = null;
		try (InputStream is = context.getResourceAsStream(path);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			byte[] b = new byte[819200];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			result = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@GetMapping("/restpicture/{id}")
	public ResponseEntity<byte[]> getPicture(@PathVariable("id") Integer id) {
		byte[] body = null;
		ResponseEntity<byte[]> re = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());

		Restaurant restaurant = restaurantService.get(id);
		if (restaurant == null) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}

		Blob blob = restaurant.getRestaurantPhoto();
		if (blob != null) {
			body = blobToByteArray(blob);
		} else {
			String path = noImage;

			body = fileToByteArray(path);
		}
		re = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);

		return re;
	}

	// 給圖用↑
	

	
	
}


