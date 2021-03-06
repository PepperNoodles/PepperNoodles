package com.infotran.springboot.companysystem.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.Session;
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
import com.infotran.springboot.loginsystem.service.UserAccountService;
import com.infotran.springboot.userAccsystem.service.UserSysService;

@Controller
@SessionAttributes(names = { "updateRestaurant", "foodTagListLength" })
public class RestaurantCRUDController {
	 @PersistenceContext
	 EntityManager em;
//	???@GetMapping("/restpicture/{id}")????????????????????????
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
	
	@Autowired
	private UserAccountService service;

	// ????????????????????????
	@GetMapping("/showAllrest")
	public String allRest(Model model) {
		model.addAttribute("restaurants", restaurantService.getAllRestaurant());
		return "company/AllRestaurants";
	}

	// ????????????????????????ByComId
	@GetMapping("/showAllrestByComId/{comId}")
	public String allRestById(Model model, @PathVariable("comId") Integer comId) {

		System.out.println("comId=" + comId);
		CompanyDetail companyDetail = companyDetailServiceImpl.findById(comId);

		UserAccount userAccount = companyDetail.getUserAccount();

		Set<Restaurant> restaurants = userAccount.getRestaurant();
		model.addAttribute("restaurants", restaurants);

		return "company/AllRestaurants";
	}

	// ??????????????????????????????
	@GetMapping("/Hours/{restIdForSettingHour}")
	public String showRestaurantBusinHourPage(Model model, @PathVariable("restIdForSettingHour") Integer id) {
		RestaurantBusinHour restaurantBusinHour = new RestaurantBusinHour();
		Restaurant restForSettingHour = restaurantService.get(id);
		restaurantBusinHour.setRestaurant(restForSettingHour);
//		System.out.println("restid====="+restaurantBusinHour.getRestaurant().getRestaurantId());
		model.addAttribute("restaurantBusinHour", restaurantBusinHour);
		model.addAttribute("restForSettingHour", restForSettingHour);
		return "company/RestaurantBusinHour";
	}

	// ajax get????????????????????????
	@GetMapping("/getHours/{restId}")
	public @ResponseBody List<RestaurantBusinHour> getHours(Model model, @PathVariable("restId") Integer id) {
		List<RestaurantBusinHour> businessHourlist = businessHourServiceImpl.businHourSByRestID(id);
		return businessHourlist;
	}

	// ajax post?????????????????????????????????????????????
	@PostMapping("/Hours/{restIdForSettingHour}")
	public @ResponseBody List<RestaurantBusinHour> addRestaurantBusinHour(
			@ModelAttribute("restaurantBusinHour") RestaurantBusinHour restaurantBusinHour, BindingResult result,
			@PathVariable("restIdForSettingHour") Integer id, Model model) {
		Restaurant restForSettingHour = restaurantService.get(id);

		// ????????????????????????????????????
		restaurantBusinHour.setRestaurant(restForSettingHour);

		// ?????????????????????????????????????????????
		Integer fk_restId = restaurantBusinHour.getRestaurant().getRestaurantId();
//		System.out.println("fk_restId="+fk_restId);
		String day = restaurantBusinHour.getDay();
		System.out.println("????????????day="+day);

		String openTime1 = restaurantBusinHour.getOpenTime();
//		System.out.println("????????????openTime1="+openTime1);

		String closeTime1 = restaurantBusinHour.getCloseTime();
//		System.out.println("????????????closeTime1="+closeTime1);

		String openTime2 = restaurantBusinHour.getOpenTime2nd();
//		System.out.println("openTime2="+openTime2);

		String closeTime2 = restaurantBusinHour.getCloseTime2nd();
//		System.out.println("closeTime2="+closeTime2);

		String openTime3 = restaurantBusinHour.getOpenTime3rd();
//		System.out.println("openTime3="+openTime3);

		String closeTime3 = restaurantBusinHour.getCloseTime3rd();
//		System.out.println("closeTime3="+closeTime3);

		// ??????DB??????????????????????????? ?????? 1.day 2.restaurantId
		RestaurantBusinHour restaurantBusinHour2 = businessHourServiceImpl.findByDayAndRestaurantId(day, fk_restId);
		if (restaurantBusinHour2 == null) {
			businessHourServiceImpl.save(restaurantBusinHour);
		} else {
			restaurantBusinHour2.setOpenTime(openTime1);
			restaurantBusinHour2.setCloseTime(closeTime1);

			restaurantBusinHour2.setOpenTime2nd(openTime2);
			restaurantBusinHour2.setCloseTime2nd(closeTime2);

			restaurantBusinHour2.setOpenTime3rd(openTime3);
			restaurantBusinHour2.setCloseTime3rd(closeTime3);

			businessHourServiceImpl.update(restaurantBusinHour2);
		}

		List<RestaurantBusinHour> businessHourlist = businessHourServiceImpl.businHourSByRestID(id);

		return businessHourlist;
	}

	@ModelAttribute
	public void foodTagListDataTest(Model model) {
		List<FoodTag> foodTagListTest = restaurantService.getAllFoodTag();
		model.addAttribute("foodTagListTest", foodTagListTest);
	}

	// ????????????????????? ???????????? ???????????????
	@GetMapping(value = "/addrest")
	public String initRestaurant(Model model) {
		Restaurant rest = new Restaurant();
		rest.setRestaurantName("????????????");
		rest.setRestaurantAddress("????????? ????????? 100???");
		rest.setRestaurantContact("02-28825252");
		rest.setRestaurantWebsite("https://www.facebook.com/");
		model.addAttribute("restaurant", rest);
		return "company/InsertRestaurant";
	}

	// ????????????
	@PostMapping(value = "/addrest")
	public String addrest(@ModelAttribute("restaurant") Restaurant rest, BindingResult result, Model model,
			HttpSession session) {

		// ????????? tag?????????
		// ??????validator??????????????????
		restvalidator.validate(rest, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println(".validate()???????????????" + error);
			}
			return "company/InsertRestaurant";
		}
		// ???model?????????
		MultipartFile picture = rest.getProductImage();
		// ??????Blob??????????????? Hibernate ???????????????
		if (picture != null && !picture.isEmpty()) {
			try {
				byte[] b = picture.getBytes();
				Blob blob = new SerialBlob(b);
				rest.setRestaurantPhoto(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("????????????????????????: " + e.getMessage());
			}
		}
		// ??????account_id
		CompanyDetail comDetail = (CompanyDetail) session.getAttribute("comDetail");
		Integer companyDetailId = comDetail.getCompanyDetailId();
		UserAccount useracc = comDetail.getUserAccount();
		// useracc???detached?????? ?????????userSysService????????????????????????
		String index = useracc.getAccountIndex();
		UserAccount user = userSysService.findByAccountIndex(index);

		rest.setUserAccount(user);

		// ???????????????????????????TAG
		Set<FoodTag> foodTagset = rest.getFoodTag();
		Iterator<FoodTag> it = foodTagset.iterator();
		while (it.hasNext()) {
			System.out.println("???????????????????????????:" + it.next().getFoodTagName());
		}

		restaurantService.save(rest);
		System.out.println("??????????????????");

		return "redirect:/showAllrestByComId/" + companyDetailId;

	}

	/* ??????????????? */
	// ???????????????????????????????????????????????????????????????????????????????????????????????????
	@GetMapping("/updateRest/{restaurantId}")
	public String showRestDataForm(@PathVariable("restaurantId") Integer id, Model model) {
		Restaurant rest = restaurantService.get(id);
		model.addAttribute("updateRestaurant", rest);
		System.out.println("??????????????????:" + id + "?????????");
		return "company/UpdateRestaurant";
	}

	// ????????????post??????????????? ?????????????????????????????? ???????????????????????????
	@PostMapping("/updateRest/{restaurantId}")
	public String modify(@ModelAttribute("updateRestaurant") Restaurant restaurant, BindingResult result, Model model,
			@PathVariable("restaurantId") Integer id, @RequestPart("productImage") MultipartFile productImage,
			HttpSession session) {
		// validator????????????
		restvalidator.validate(restaurant, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("????????????" + error);
			}
			// ????????? ??????????????????
			return "company/UpdateRestaurant";
		}
		// ????????????????????????????????????

		if (productImage != null && !productImage.isEmpty()) {
			try {
				byte[] b = productImage.getBytes();
				Blob blob = new SerialBlob(b);
				restaurant.setRestaurantPhoto(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("????????????????????????: " + e.getMessage());
			}
			restaurantService.update(restaurant);
		} else {
			Blob oldRestPhoto = restaurantService.get(restaurant.getRestaurantId()).getRestaurantPhoto();
			restaurant.setRestaurantPhoto(oldRestPhoto);
			restaurantService.update(restaurant);
		}
		CompanyDetail comDetail = (CompanyDetail) session.getAttribute("comDetail");
		Integer companyDetailId = comDetail.getCompanyDetailId();
		return "redirect:/showAllrestByComId/" + companyDetailId;
	}
	/* ??????????????? */

	// ????????????
	@DeleteMapping("/deleteRest/{Id}")
	public String deleterest(@PathVariable("Id") Integer id,HttpSession session) {
		
		
	
		Restaurant restToDelete = restaurantService.get(id);
		
//		?????????????????????
		List<RestaurantBusinHour> hourlist = businessHourServiceImpl.findRestaurantBusinHourIdByRestaurantId(id);
		
		for (RestaurantBusinHour restaurantBusinHour : hourlist) {
			
		
//			System.out.println("----????????????ID="+restaurantBusinHour.getRestaurantBusinHourId());
			
			businessHourServiceImpl.deleteByID(restaurantBusinHour.getRestaurantBusinHourId());
		}
		
			
		//?????????????????????????????? ???????????????AccountUser+Role???comapny????????????== ???!
		restToDelete.setFoodTag(null);
		restToDelete.setUserAccount(null);
		List<UserAccount> users = restToDelete.getUsers();
		for (UserAccount user:users) {
			List<Restaurant> collections = user.getRestaurantCollections();
			for (int i = 0;i<collections.size();i++) {
				if(collections.get(i).getRestaurantId() ==restToDelete.getRestaurantId()) {
					collections.remove(i);
				}				
			}
			service.update(user);
			
		}
		
		
		//restToDelete.setUsers(Collections.emptyList());
		System.out.println("?????????:" + id + "?????????companyID???foodTag??????null---???????????????");
		restaurantService.update(restToDelete);
		
		 Session session2 = (Session) em.getDelegate();
		 session2.clear();
		 
		System.out.println("???????????????:" + id + "?????????!!");
		restaurantService.delete(id);	
		CompanyDetail comDetail=(CompanyDetail) session.getAttribute("comDetail");
		Integer companyDetailId = comDetail.getCompanyDetailId();
		return "redirect:/showAllrestByComId/"+companyDetailId;
	}

	// check????????????????????????
	@PostMapping("/checkRAddress")
	public @ResponseBody Map<String, String> checkRAddress(@RequestParam("value") String rAdd) {
		System.out.println("????????????:" + rAdd);
		Restaurant rest = restaurantService.findByRestaurantAddress(rAdd);
		Map<String, String> map = new HashMap<String, String>();

		try {
			String restName = rest.getRestaurantName();
			System.out.println("???????????????" + restName);
			map.put("result", "???????????????????????? ????????????");
			map.put("checkboolean", "false");
		} catch (NullPointerException e) {
			map.put("result", "?????????????????????");
			map.put("checkboolean", "true");
		}
		System.out.println(map.get("result"));
		return map;
	}

	// ??????????????????2
	@GetMapping("/restTag2/{id}")
	public @ResponseBody Set<FoodTag> restTag2(@PathVariable("id") Integer id) {
		Restaurant rest = restaurantService.get(id);
		Set<FoodTag> set = rest.getFoodTag();
		return set;

	}

	// ????????????
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

	// ????????????

}
