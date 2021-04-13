package com.infotran.springboot.loginsystem.controller;

import java.io.File;
import java.sql.Blob;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.FoodTagUser;
import com.infotran.springboot.commonmodel.SendEmail;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.commonmodel.UserDetail;
import com.infotran.springboot.loginsystem.dao.FoodTagRepository;
import com.infotran.springboot.loginsystem.dao.UserAccountRepository;
import com.infotran.springboot.loginsystem.service.UserAccountService;


@Controller
@SessionAttributes(names = "useraccount")
public class UserAccountController {
	
	String imageRootDirectory = "D:\\_SpringBoot\\image";
	
	File imageFolder = null; 
	
	//test
	@Autowired
	private FoodTagRepository foodTagRepository;
	
	@Autowired
	private UserAccountService service;
	
	@Autowired
	private UserAccountRepository usp;
//	
//	@Autowired
//	private UserAccount useraccount1;
//
//	@Autowired
//	private UserDetail userDetail;
//	
	@Autowired
	private SendEmail sendemail;

	public UserAccountController() {
		imageFolder = new File(imageRootDirectory, "images");
		if ( !imageFolder.exists() )
			imageFolder.mkdirs();
	}
	
	
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/webapp/imagedata";

	@PostMapping(value="/members",consumes={"multipart/mixed","multipart/form-data","application/json"})
	public @ResponseBody Map<String,String> Registure( @RequestPart("userinfo")String toString,
			@RequestPart("file")MultipartFile file,
			HttpServletResponse response,
			Model m,
			HttpSession ses,
			SessionStatus s) throws Exception{
		UserAccount useraccount1 = new UserAccount();
		UserDetail userDetail = new UserDetail();

		Map<String, String> map = new HashMap<>();
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});

		ses.removeAttribute("useraccount");
		System.out.println("fuck======================================================0");
//		UserAccount useraccount1 = new UserAccount();
		useraccount1.setAccountIndex(dispatch.get("accountIndex"));
		useraccount1.setPassword(dispatch.get("password"));
		userDetail.setRealName(dispatch.get("realName"));
		userDetail.setNickName(dispatch.get("nickname"));
		userDetail.setPhoneNumber(dispatch.get("phoneNumber"));
		userDetail.setBirthDay(dispatch.get("birthDay"));
		userDetail.setGender(dispatch.get("gender"));
		userDetail.setLocation(dispatch.get("location"));
		System.out.println("fuck======================================================1");
		String originalFilename = file.getOriginalFilename();
		String extFilename = ""; 
		if (originalFilename.length() > 0 && originalFilename.lastIndexOf(".") > -1) {
			extFilename = originalFilename.substring(originalFilename.lastIndexOf("."));
			System.out.println(extFilename);
			System.out.println("fuck======================================================2");
		}
		if (file != null && !file.isEmpty()) {
			try {
				byte[] b = file.getBytes();
				Blob blob = new SerialBlob(b);
				userDetail.setUserPhoto(blob);
				System.out.println("fuck======================================================3");
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		useraccount1.setUserAccountDetail(userDetail);
//		m.addAttribute("useraccount",useraccount1);
		Integer flag;
		System.out.println("fuck======================================================4");
		try {
			ses.setAttribute("useraccount",useraccount1);
			m.addAttribute("useraccount",useraccount1);
			flag = service.save(useraccount1); 
			System.out.println("fuck======================================================5");
			if(flag ==1) {
				map.put("success", "新增成功");
				System.out.println("fuck======================================================6");
				response.setStatus(HttpServletResponse.SC_CREATED);
			}else if(flag==-1) {
				map.put("fail", "帳號重複");
			}
		}catch (Exception e) {
			map.put("fail2", e.getMessage());
		}
		
		try {
			File fileplace = new File(imageFolder, "UserImage_" + useraccount1.getAccountIndex() + extFilename);
			file.transferTo(fileplace);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
		}
		return map;
	}
	
	
	@PostMapping(value="/CheckMemberAccount",produces="application/json")
	public @ResponseBody Map<String,String> checkUserAccount(@RequestBody UserAccount user){
		Map<String, String> map = new HashMap<>();
		String userAccountName = service.checkUserAccount(user.getAccountIndex());
		map.put("username",userAccountName );
		return map;
	}
	
	@GetMapping(value="/CheckMemberAccountinterests/{userid}",produces="application/json")
	public @ResponseBody Map<String,String> CheckMemberAccountinterests(@PathVariable("userid") Integer userid){
		Map<String, String> map = new HashMap<>();
		List<UserAccount> userAccounts = usp.findByAccountId(userid);
		UserAccount userAccount  = userAccounts.get(0);
		Set<FoodTagUser> FoodTagUsers= new HashSet<>();
		
//		Iterator<FoodTagUser> iter = FoodTagUsers.iterator(); 
//		while (iter.hasNext()) { 
//		  Object x = iter.next(); 
//		  System.out.println(x.toString());
//		} 

		FoodTagUsers =userAccount.getFoodTagUsers();
		for(int i=0;i<FoodTagUsers.size();i++){
		     System.out.println(FoodTagUsers.size());
		}
		map.put("userinterest",	FoodTagUsers.toString() );
		return map;
	}
	

	
	@GetMapping(value="/members")
	public List<UserAccount> queryAllUsers(){
		List<UserAccount> userlist = service.getAllMembers();
		return userlist;
	}
	
	@PostMapping(value="/sendEmail",produces="application/json")
	public  @ResponseBody Map<String,String> sendEmail(@RequestBody UserAccount user){
		System.out.println("------controller-------"+user.getAccountIndex());
		Map<String,String> map = new HashMap<String, String>();
		String code = sendemail.getRandom();
		System.out.println(code);
		user.setCode(code);
		boolean emailResult = sendemail.sendEmail(user);
		if (emailResult) {
			map.put("emailCode", code);
		}
		return map;
	}
	@PostMapping(value="/addAccountInterest",consumes="application/json")
	public @ResponseBody Map<String,String> saveAccountInterest(
			/*取account sessionattribute 值*/ @ModelAttribute("useraccount")UserAccount userAccount,
			@RequestBody String[] interest, HttpServletResponse res,
			Model m,
			SessionStatus s,
			HttpSession ses){
		Map<String,String> map = new HashMap<>();

		if(interest.length!=0) {
			
		
		System.out.println(userAccount.getAccountIndex());
		Set<FoodTagUser> userSet = userAccount.getFoodTagUsers();
		
			for(int i = 0; i<interest.length; i++) {
				System.out.println("json傳到server的值" + interest[i]);
				String tagName = interest[i];
				
				//用json轉的字串值取得DB的 FoodTag
				FoodTag tag = foodTagRepository.findByFoodTagName(tagName);
				//驗證 FoodTag 資訊
				System.out.println("FoodTag Object's  tag = " + tag.getFoodTagName());
				
				//設定一筆關聯表資料
				FoodTagUser ftu = new FoodTagUser();
				//分別將雙邊資料存入
				ftu.setFkfoodtag(tag);
				ftu.setFkuser(userAccount);
				
				userSet.add(ftu);
				Set<FoodTagUser> tagSet =  tag.getFoodTagUsers();
				tagSet.add(ftu);
				System.out.println("save foodtag");
				foodTagRepository.save(tag);
				map.put("success","1");
	
			}
			
		}else {
			map.put("success","0");
		}

		return map;
	}
	
	
}
