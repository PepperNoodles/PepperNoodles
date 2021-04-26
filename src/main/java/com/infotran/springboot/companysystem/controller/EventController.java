package com.infotran.springboot.companysystem.controller;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.EventList;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.service.EventListService;
import com.infotran.springboot.companysystem.service.RestaurantService;

@Controller
public class EventController {
	
	@Autowired
	EventListService eventListService;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@GetMapping("/event")
	public String event(Model model , Integer restId) {
		restId = 1;
		Restaurant restaurant = restaurantService.findById(restId);
		List<EventList> events =  eventListService.getByRest(restaurant);
		model.addAttribute("events", events);	
		return "event/addEvent";
	}
	
	@PostMapping("/addEvent")
	public @ResponseBody String addEvent(@RequestPart("eventInfo")String toString,
										 @RequestPart("file")MultipartFile file,
										 EventList event , Model model) throws JsonMappingException, JsonProcessingException, ParseException {
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		Integer restId = Integer.valueOf(dispatch.get("restaurantId"));
		Restaurant restaurant = restaurantService.findById(restId);
		event.setRestaurant(restaurant);
		event.setEventName(dispatch.get("eventName"));
		
		//取得字串Date
		String eventStartDate = dispatch.get("eventStartDate");
		String eventEndDate = dispatch.get("eventEndDate");
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		//進行轉換
		Date startDateT = sdf.parse(eventStartDate);
		Date endDateT = sdf.parse(eventEndDate);
 		//去掉時間
		java.sql.Date startDate = new java.sql.Date(startDateT.getTime());
		java.sql.Date endDate = new java.sql.Date(endDateT.getTime());
		event.setEventStartDate(startDate);
		event.setEventEndDate(endDate);
		event.setContent(dispatch.get("content"));
		if (file != null && !file.isEmpty()) {
			try {
				byte[] b = file.getBytes();
				Blob blob = new SerialBlob(b);
				event.setEventPicture(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		eventListService.insert(event);
		return toString;
	}
	
	//秀照片
	@GetMapping(path="/getEventPicture/{eventId}",produces = "image/jpeg")
	public ResponseEntity<byte[]> getEventId(@PathVariable("eventId") Integer eventId) {
		EventList event = eventListService.findById(eventId);
		Blob photo=null;
		byte[] eventPhoto=null;
		photo = event.getEventPicture();
		try {
			int blobLength = (int) photo.length();  
			eventPhoto = photo.getBytes(1, blobLength);
			photo.free();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(eventPhoto,headers,HttpStatus.OK);		
	}
	
	
	
	
	
}
