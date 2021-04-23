package com.infotran.springboot.companysystem.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.companysystem.validator.RestaurantValidator;
import com.infotran.springboot.userAccsystem.service.UserSysService;

@Controller
@SessionAttributes(names = {"updateRestaurant"} )
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
	
	
	// 顯示所有餐廳資料
	@GetMapping("/showAllrest")
	public String list(Model model) {
		model.addAttribute("restaurants", restaurantService.getAllRestaurant());
		System.out.println("showall"+model.getAttribute("restaurants"));
		return "company/AllRestaurants";
	}
	
	
	
	// 連進新增餐廳時 給預設值 回傳新增頁
	@GetMapping(value = "/addrest")
	public String initRestaurant(Model model) {
		Restaurant rest= new Restaurant();
		rest.setRestaurantName("幽靈炒飯");
		rest.setRestaurantAddress("台北興安街");
		rest.setRestaurantContact("0909053909");
		rest.setRestaurantWebsite("facebook.com");
		model.addAttribute("restaurant", rest);
		return "company/InsertRestaurant";
	}


	// 新增餐廳
	@PostMapping(value = "/addrest")
	public String addrest(@ModelAttribute("restaurant") Restaurant rest, BindingResult result, Model model,
			HttpSession session) {
		
		
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
//		綁定account_id
		CompanyDetail comDetail=(CompanyDetail) session.getAttribute("comDetail");
		UserAccount useracc=comDetail.getUserAccount();
//		useracc是detached狀態 必須用userSysService在抓一次
		String index=useracc.getAccountIndex();
		UserAccount user = userSysService.findByAccountIndex(index);
		//String index=useracc.getAccountIndex();
		rest.setUserAccount(user);  //會報錯detached entity passed to persist:com.infotran.springboot.commonmodel.UserAccount
		

		restaurantService.save(rest);
		System.out.println("新增成功");
		return "redirect:/showAllrest";

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
			@PathVariable("restaurantId") Integer id,@RequestPart("productImage")MultipartFile productImage ) {
		System.out.println("餐廳名稱"+restaurant.getRestaurantName());
		// validator檢查錯誤
		restvalidator.validate(restaurant, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			//若有錯 回到修改頁面
			return "company/UpdateRestaurant";
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
		
		return "redirect:/showAllrest";
	}
/*更新餐廳↑*/
	
	
	// 刪除餐廳
	@PostMapping("/deleteRest/{Id}")
	public String deleterest(@PathVariable("Id") Integer id) {
		restaurantService.delete(id);
		System.out.println("刪除了編號:" + id + "的餐廳!!");
		return "redirect:/showAllrest";
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
