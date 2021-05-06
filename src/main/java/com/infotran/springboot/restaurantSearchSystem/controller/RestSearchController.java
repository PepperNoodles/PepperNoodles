package com.infotran.springboot.restaurantSearchSystem.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	//一般會員連的評論區
	@GetMapping("/userSingleRestPage/{restId}")
	public String userMessage(@PathVariable("restId") Integer restId , Model model) {
		Restaurant rest =  restSearchService.getById(restId);
		model.addAttribute("rest", rest);
		return "mappage/singleRest";
	}
	
	
	@GetMapping(path="/map",produces = "application/json;charset=UTF-8" )
	public String indexSearch(@RequestParam(name="restName",defaultValue = "") String searchName,
			 				@RequestParam(name="searchTag",defaultValue = "") String searchTag,
			 				Model model){
		System.out.println(searchName+searchTag);
		ObjectMapper mapper = new ObjectMapper();
		if(searchTag.equals("NULL")) {			
			List<Restaurant> rests = restSearchService.findRestaurantNameLike(searchName);
//			System.out.println(searchName);
//			System.out.println("======="+rests.size());
			try {
				String jsonString = mapper.writeValueAsString(rests);
				model.addAttribute("rests",jsonString);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println(searchName+searchTag);
			List<Restaurant> rests = restSearchService.findSearchNameAndTag(searchName, searchTag);
			System.out.println("======="+rests.size());
			try {
				String jsonString = mapper.writeValueAsString(rests);
				System.out.println(jsonString);
				model.addAttribute("rests",jsonString);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		
		//System.out.println("so far so good");
		return "mappage/showmap";
	}
	
	
	@GetMapping(path="/restId/{id}",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public Restaurant seachRestById(@PathVariable(name="id") Integer restId) {
		System.out.println("seachByName");
		Restaurant rests = restSearchService.getById(restId);
		return rests;
	}
	
	@GetMapping(path="/restName",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public List<Restaurant> seachRestByName(@RequestParam(name="restName",defaultValue = "") String restName) {
		List<Restaurant> rests = restSearchService.findRestaurantNameLike(restName);	
	
		
		return rests;
	}
	
	@GetMapping(path="/restAddr",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public List<Restaurant> seachRestByNameAddr(@RequestParam(name="restAddr",defaultValue = "") String restAddr){
		List<Restaurant> rests = restSearchService.findAddressNameLike(restAddr);
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
	
	@GetMapping(path="/restNear/{bigLat}/{smallLat}/{bigLong}/{smallLong}",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public List<Restaurant> RestNearMap(@PathVariable String bigLat,@PathVariable String smallLat,@PathVariable String bigLong,@PathVariable String smallLong){
		
		BigDecimal bigLatD= new BigDecimal(bigLat);
		BigDecimal smallLatD=new BigDecimal(smallLat);
		BigDecimal bigLongD=new BigDecimal(bigLong);
		BigDecimal smallLongD=new BigDecimal(smallLong);
		System.out.println(bigLatD);
		System.out.println(smallLatD);
		List<Restaurant> rests = restSearchService.findNearMap(smallLatD,bigLatD,smallLongD,bigLongD);
		//List<Restaurant> restss = restSearchService.findNearMapLat(bigLatD, smallLatD);
		return rests;
	}
	
	@GetMapping(path="/tagAll",produces = "application/json;charset=UTF-8" )
	@ResponseBody
	public List<String> findAllTag(){
		List<FoodTag> tags = restTagService.findAll();
		
		List<String> tagsName = new ArrayList<>();
		for(int i = 0 ; i< tags.size();i++) {
			tagsName.add(tags.get(i).getFoodTagName());
		}	
		
		return tagsName;
	}
	
	//Search區域
	@GetMapping(path="/restSearchandTag",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Restaurant> restSearchandTag(@RequestParam(name="restName",defaultValue = "") String searchName,
											 @RequestParam(name="searchTag",defaultValue = "") String searchTag){
		List<Restaurant> rests = restSearchService.findSearchNameAndTag(searchName, searchTag);
		return rests;
	}
	
	@GetMapping(path="/restSearchandDist",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Restaurant> restSearchandDist(@RequestParam(name="restName",defaultValue = "") String searchName,
											 @RequestParam(name="searchDist",defaultValue = "") String searchdist){
		List<Restaurant> rests = restSearchService.findASearchtNameAndDistName(searchName, searchdist);
		return rests;
	}
	
	@GetMapping(path="/restSearchandDistandTag",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Restaurant> restSearchandDistandTag(@RequestParam(name="restName",defaultValue = "") String searchName,
											 		@RequestParam(name="searchDist",defaultValue = "") String searchdist,
											 		@RequestParam(name="searchTag",defaultValue = "") String searchTag){
		List<Restaurant> rests = restSearchService.findSearchNameAndDistAndTag(searchName, searchdist, searchTag);
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
