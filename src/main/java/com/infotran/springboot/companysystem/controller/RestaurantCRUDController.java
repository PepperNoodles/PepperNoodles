package com.infotran.springboot.companysystem.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.companysystem.validator.RestaurantValidator;

@Controller
public class RestaurantCRUDController {
	String noImage = "/images/NoImage/NoImage_restaurant.jpg";
	
	@Autowired
	ServletContext context;

	@Autowired
	RestaurantService restaurantService; // 介面當作參用

//給圖用↓
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

	@GetMapping("/picture/{id}")
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
			String path = null;

			body = fileToByteArray(path);
		}
		re = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);

		return re;
	}

//給圖用圖用↑
	@GetMapping(value = "/addrest")
	public String initRestaurant(Model model) {
		Restaurant rest = new Restaurant();
		model.addAttribute("restaurant", rest);
		rest.setRestaurantName("幽靈炒飯");
		rest.setRestaurantAddress("台北興安街");
		rest.setRestaurantContact("0909053909");
		rest.setRestaurantWebsite("facebook.com");
		return "companySystem/Insertrestaurant";

	}

	// 新增餐廳
	@PostMapping(value = "/addrest")
	public String addrest(@ModelAttribute("restaurant") Restaurant rest, BindingResult result, Model model,
			HttpServletRequest request) {

		// 建立餐廳validator進行資料檢查
		RestaurantValidator restvalidator = new RestaurantValidator();
		restvalidator.validate(rest, result);
		if (result.hasErrors()) {
//          下列敘述可以理解Spring MVC如何處理錯誤				
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			return "companySystem/Insertrestaurant";
		}

		Blob blob = rest.getRestaurantPhoto();

		restaurantService.save(rest);
		System.out.println("新增成功");
		return "/template/template";

	}
}
