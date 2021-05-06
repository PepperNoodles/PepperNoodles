package com.infotran.springboot.companysystem.controller;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	//新增菜單
	@GetMapping("/menu/{restId}")
	public String menu(Model model , @PathVariable("restId")Integer restId) {
		Restaurant restaurant = restaurantService.findById(restId);
		List <MenuDetail> menus =  MenuDetailService.getByRest(restaurant);
		model.addAttribute("menus", menus);
		model.addAttribute("restId", restId);
		return "company/addMenu";
	}
	
	//修改菜單
	@GetMapping("/update/{menuId}")
	public String update(Model model , @PathVariable("menuId")Integer menuId) {
		MenuDetail updateMenu = MenuDetailService.findById(menuId);
		Integer restId = updateMenu.getRestaurant().getRestaurantId();
		Restaurant restaurant = restaurantService.findById(restId);
		List <MenuDetail> menus =  MenuDetailService.getByRest(restaurant);
		model.addAttribute("menus", menus);
		model.addAttribute("restId", restId);
		model.addAttribute("updateMenu", updateMenu);
		return "company/updateMenu";
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
					MenuDetail newMenu = MenuDetailService.insert(menu);
					model.addAttribute("success",newMenu.getMenuDetailId());
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
				}
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return "/rest/menu/" ;
	}
	
	//秀某餐廳的全部菜單
	@GetMapping("/showMenu")
	public String showMenu(Model model , Integer restId) {
		restId = 1;
		Restaurant restaurant = restaurantService.findById(restId);
		List <MenuDetail> menus =  MenuDetailService.getByRest(restaurant);
		model.addAttribute("menus", menus);
		for (MenuDetail menuDetail : menus) {
			
			System.out.println(menuDetail.getMenuDetailId());
			
		}
		return "company/showMenu";
	}
	
	//用menu的id找回database menu照片
	@GetMapping(path="/getMenuPicture/{menuDetailId}",produces = "image/jpeg")
	public ResponseEntity<byte[]> getMenusId(@PathVariable("menuDetailId") Integer menuDetailId) {
		MenuDetail menuDetail = MenuDetailService.findById(menuDetailId);
		Blob photo=null;
		byte[] menuPhoto=null;
		photo = menuDetail.getDishImage();
		try {
			int blobLength = (int) photo.length();  
			menuPhoto = photo.getBytes(1, blobLength);
			photo.free();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(menuPhoto,headers,HttpStatus.OK);		
	}
	
	//用id刪除database user照片
	@DeleteMapping("/deleteMenuPicture/{menuDetailId}")
	public String deleteMenuId(@PathVariable("menuDetailId") Integer menuDetailId) {
		System.out.println("刪圖片");
		MenuDetail menuDetail = MenuDetailService.findById(menuDetailId);
		Integer restaurantId = menuDetail.getRestaurant().getRestaurantId();
		System.out.println("restaurantId:"+restaurantId);
		menuDetail.setRestaurant(null);
		MenuDetailService.deleteById(menuDetailId);
		return "redirect:/rest/menu/"+restaurantId;
	}
		
}
