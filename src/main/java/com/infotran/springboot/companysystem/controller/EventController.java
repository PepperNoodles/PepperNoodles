package com.infotran.springboot.companysystem.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.EventList;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.impl.EventListServiceImpl;
import com.infotran.springboot.companysystem.service.impl.RestaurantServiceImpl;

@Controller
@SessionAttributes(names = {"event","events"})
public class EventController {
	
	String NoEvent = "/images/company/presetEvent.jpg";
	
	@Autowired
	ServletContext context;
	
	@Autowired
	private EventListServiceImpl eventListService;
	
	@Autowired
	private RestaurantServiceImpl restaurantService;
	
	
	@GetMapping("/showEvent")
	public String blog() {
		return "event/showEvent";
	}
	
	@GetMapping("/notOverEvent")
	@ResponseBody
	public List<EventList> notOverEvent() throws ParseException {
		List<EventList> allEvent = eventListService.getAllEvents();
		List<EventList> notOverEvent = new ArrayList<EventList>();
		for(int i =0; i<allEvent.size(); i++) {
			Date endTime = allEvent.get(i).getEventEndDate();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String currentDateS = sdf.format(new Date());
			Date currentDate = sdf.parse(currentDateS);
			
			System.out.println("活動結束時間:"+endTime);
			System.out.println("現在時間時間:"+currentDate);
			
//			ParsePosition pos = new ParsePosition(8);  
//			Date currentTime_2 = formatter.parse(dateString, pos);  
			
			if (currentDate.before(endTime)) {
				System.out.println("結束的活動:"+allEvent.get(i).getEventId()+" ,結束日期:"+allEvent.get(i).getEventEndDate());
			  }else {
				  System.out.println("尚未結束的活動:"+allEvent.get(i).getEventId()+" ,結束日期:"+allEvent.get(i).getEventEndDate());
				  notOverEvent.add(allEvent.get(i));
			  }

			
		}
		return notOverEvent;
	}
	
	
	@GetMapping("/event/{restId}")
	public String event(Model model ,@PathVariable("restId") Integer restId) {
//		restId = 1;
		Restaurant restaurant = restaurantService.findById(restId);
		UserAccount account = restaurant.getUserAccount();
		Set<Restaurant> rests = account.getRestaurant();
		List<EventList> events =  eventListService.getByRest(restaurant);
		
		model.addAttribute("rests", rests);	
		model.addAttribute("restId", restId);	
		model.addAttribute("events", events);	
		return "event/addEvent";
	}
	
	@PostMapping("/addEvent")
	public @ResponseBody String addEvent(@RequestPart("eventInfo")String toString,
										 @RequestPart("file")MultipartFile file,
										 EventList event , Model model) throws JsonMappingException, JsonProcessingException, ParseException {
		System.out.println("進入接收活動的Controller");
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		Integer restId = Integer.valueOf(dispatch.get("restaurantId"));
		Restaurant restaurant = restaurantService.findById(restId);
		event.setRestaurant(restaurant);
		event.setEventName(dispatch.get("eventName"));
		//取得字串轉Date
		String eventStartDate = dispatch.get("eventStartDate");
		String eventEndDate = dispatch.get("eventEndDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDateT = sdf.parse(eventStartDate);
		Date endDateT = sdf.parse(eventEndDate);
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
	
	@GetMapping(value = {"/findUpdateEvent/{eventId}"} )
	public String findUpdateEvent(@PathVariable("eventId") Integer eventId , Model model) {
		EventList event = eventListService.findById(eventId);
		Integer restId = event.getRestaurant().getRestaurantId();
		Restaurant restaurant = restaurantService.findById(restId);
		List<EventList> events =  eventListService.getByRest(restaurant);
		model.addAttribute("restId",restId);
		model.addAttribute("event",	event);
		model.addAttribute("events", events);	
//		return "event/addEvent";
		return "event/updateEvent";
	}
	
	@PostMapping("/updateEvent")
	public @ResponseBody String updateEvent(@RequestPart("eventInfo")String toString,
										    @RequestPart(name = "file",required = false)MultipartFile file,
										    Model model) throws Exception {
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		Integer eventId = Integer.valueOf(dispatch.get("eventId"));
		EventList event = eventListService.findById(eventId);
		event.setEventName(dispatch.get("eventName"));
		//取得字串轉Date
		String eventStartDate = dispatch.get("eventStartDate");
		String eventEndDate = dispatch.get("eventEndDate");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDateT = dateFormat.parse(eventStartDate);
		Date endDateT = dateFormat.parse(eventEndDate);
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
		eventListService.update(event);
		return toString;
	}
	
	@DeleteMapping("/deleteEvent/{eventId}")
	public String deleteMenuId(@PathVariable("eventId") Integer eventId) {
		EventList event = eventListService.findById(eventId);
		Integer restId = event.getRestaurant().getRestaurantId();
		event.setRestaurant(null);
		eventListService.deleteById(eventId);
		return "redirect:/event/"+restId;
	}
	
	//秀照片
	@GetMapping(path="/getEventPicture/{eventId}",produces = "image/jpeg")
	public ResponseEntity<byte[]> getEventId(@PathVariable("eventId") Integer eventId) throws SQLException {
		EventList event = eventListService.findById(eventId);
		Blob photo=null;
		byte[] eventPhoto=null;
		photo = event.getEventPicture();
		if(photo == null) {
			eventPhoto = fileToEvent(NoEvent);
		}
		else {
			int blobLength = (int) photo.length();  
			eventPhoto = photo.getBytes(1, blobLength);
			photo.free();
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(eventPhoto,headers,HttpStatus.OK);		
	}

	private byte[] fileToEvent(String NoEvent) {
		byte[] result = null;
		try (InputStream is = context.getResourceAsStream(NoEvent);
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
	
	
}
