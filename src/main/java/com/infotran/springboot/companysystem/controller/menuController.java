package com.infotran.springboot.companysystem.controller;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.companysystem.service.impl.MenuDetailServiceImpl;

@Controller
@RequestMapping("/rest")
public class menuController {
	
	@Autowired
	private MenuDetailServiceImpl MenuDetailService;	
	
	@Autowired
	private RestaurantService restaurantService;
	
	@GetMapping("/menu")
	public String menu(Model model) {
		return "company/addMenu";
	}
	
	@PostMapping("/addMenu")
	public @ResponseBody String addMenu(@RequestPart("restInfo")String toString,
			              			   @RequestPart("file")MultipartFile file,
			              			   MenuDetail menu , Model model) {
		try {
			Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
			Integer restId = Integer.valueOf(dispatch.get("restaurantId"));
			System.out.println("新增菜單的餐廳id:"+restId);
			//找到菜單所屬餐廳
			Restaurant restaurant = restaurantService.findById(restId);
			menu.setRestaurant(restaurant);
			
			if (file != null && !file.isEmpty()) {
				try {
					byte[] b = file.getBytes();
					Blob blob = new SerialBlob(b);
					menu.setDishImage(blob);
					MenuDetailService.insert(menu);
					model.addAttribute("success","新增成功");
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
				}
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return "success" ;
	}
}
