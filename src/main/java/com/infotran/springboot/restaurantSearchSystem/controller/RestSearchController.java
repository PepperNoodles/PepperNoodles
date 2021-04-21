package com.infotran.springboot.restaurantSearchSystem.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.restaurantSearchSystem.service.RestSearchService;
import com.infotran.springboot.restaurantSearchSystem.service.RestTagService;


@Controller
@RequestMapping("/restSearch")
public class RestSearchController {

	@Autowired
	private RestSearchService restSearchService;
	
	@Autowired
	private RestTagService restTagService;
	
	@Autowired
	ServletContext servletContext;
	
	@GetMapping(path="/restName",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public List<Restaurant> seachRestByName() {
		String restName="我";
		System.out.println("seachByName");
		List<Restaurant> rests = restSearchService.findRestaurantNameLike(restName);		
		return rests;
	}
	
	@GetMapping(path="/restAddr",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public List<Restaurant> seachRestByNameAddr(){
		String addr ="台北";
		List<Restaurant> rests = restSearchService.findAddressNameLike(addr);
		return rests;
	}
	
	@GetMapping(path="/restTag",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public Set<Restaurant> searchRestByTag(){
		FoodTag foodtag = restTagService.findById(3);
		Set<Restaurant> rests = foodtag.getRestaurant();
		return rests;
	}

	@GetMapping(path="/restTagAndName",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public List<Restaurant> searchRestByTagAndName(){
		List<Restaurant> rests = restSearchService.findNameAndTag("泰", "pizza");
		return rests;
	}
	
	@GetMapping(path="/restAll",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public List<Restaurant> searchAll(){
		List<Restaurant> rests = restSearchService.findAll();
		return rests;
	}
	
	@GetMapping(path="/restPicByid/{restid}",produces = "image/jpeg" )
	public ResponseEntity<byte[]> restPicByid(@PathVariable(name="restid") Integer id){
		Restaurant rest = restSearchService.getById(id);
		Blob photo = rest.getRestaurantPhoto();
		byte[] rPhoto = null;
		if(photo!=null) {	 		
			try {
				int blobLength = (int) photo.length();  
				rPhoto = photo.getBytes(1, blobLength);
				photo.free();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		else {
			String path = servletContext.getRealPath("/");
			path+="images/restaurantCRUD/defaultRest.png";
			System.out.println(path);
			File noimage = new File(path);
			try {
				rPhoto=FileUtils.readFileToByteArray(noimage);				
				} catch (IOException e) {e.printStackTrace();}
		}		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(rPhoto,headers,HttpStatus.OK);	

	}
}
