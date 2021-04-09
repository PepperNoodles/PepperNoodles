package com.infotran.springboot.loginsystem.controller;

import java.io.File;
import java.sql.Blob;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.SendEmail;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.commonmodel.UserDetail;
import com.infotran.springboot.loginsystem.service.UserAccountService;


@Controller
@SessionAttributes(names = "useraccount")
public class UserAccountController {
	
	String imageRootDirectory = "D:\\_SpringBoot\\image";
	
	File imageFolder = null; 
	
	@Autowired
	private UserAccountService service;
	
	@Autowired
	private UserAccount useraccount;

	@Autowired
	private UserDetail userDetail;
	
	@Autowired
	private SendEmail sendemail;

	public UserAccountController() {
		imageFolder = new File(imageRootDirectory, "images");
		if ( !imageFolder.exists() )
			imageFolder.mkdirs();
	}
	
	
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/webapp/imagedata";

	@PostMapping(value="/members",consumes={"multipart/mixed","multipart/form-data","application/json"})
	public @ResponseBody Map<String,String> Registure( @RequestPart("userinfo")String toString,@RequestPart("file")MultipartFile file,HttpServletResponse response, Model m) throws Exception{
		Map<String, String> map = new HashMap<>();
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		
		useraccount.setAccountIndex(dispatch.get("accountIndex"));
		useraccount.setPassword(dispatch.get("password"));
		userDetail.setRealName(dispatch.get("realName"));
		userDetail.setNickName(dispatch.get("nickname"));
		userDetail.setPhoneNumber(dispatch.get("phoneNumber"));
		userDetail.setBirthDay(dispatch.get("birthDay"));
		userDetail.setGender(dispatch.get("gender"));
		userDetail.setLocation(dispatch.get("location"));
		
		String originalFilename = file.getOriginalFilename();
		String extFilename = ""; 
		if (originalFilename.length() > 0 && originalFilename.lastIndexOf(".") > -1) {
			extFilename = originalFilename.substring(originalFilename.lastIndexOf("."));
			System.out.println(extFilename);
		}
		if (file != null && !file.isEmpty()) {
			try {
				byte[] b = file.getBytes();
				Blob blob = new SerialBlob(b);
				userDetail.setUserPhoto(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		useraccount.setUserAccountDetail(userDetail);
		Integer flag;
		try {
			flag = service.save(useraccount);
			if(flag ==1) {
				map.put("success", "新增成功");
				m.addAttribute("useraccount",useraccount);
				response.setStatus(HttpServletResponse.SC_CREATED);
			}else if(flag==-1) {
				map.put("fail", "帳號重複");
			}
		}catch (Exception e) {
			map.put("fail2", e.getMessage());
		}
		
		try {
			File fileplace = new File(imageFolder, "UserImage_" + useraccount.getAccountIndex() + extFilename);
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
	public @ResponseBody Map<String,String> saveAccountInterest(@ModelAttribute("useraccount")UserAccount userAccount,@RequestBody HashSet<FoodTag> actDe, HttpServletResponse res, Model m){
		System.out.println(userAccount);
		System.out.println(actDe);
		Map<String,String> map = new HashMap<>();
		int n = 0;
		try {
//			n = accountDetailAJService.saveAccountDetailAJ(actDe);
//			if (n == 1) {
			if(actDe!=null) {
				n=1;
				map.put("success", "Detail資訊新增成功");
				res.setStatus(HttpServletResponse.SC_CREATED);
				int x = service.addAccountAJDetail(member, actDe);
				System.out.println("x: " + x);
			} else if (n == -1) {
				map.put("fail", "帳號重複");
			}
		} catch (Exception e) {
			map.put("fail", e.getMessage());
		}
		return map;
	}
	
	
}
