package com.infotran.springboot.companysystem.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.infotran.springboot.commonmodel.CompanyDetail;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.CompanyDetailService;
import com.infotran.springboot.loginsystem.service.UserAccountService;

@Controller
@SessionAttributes(names = {"comDetail"})
public class CompanyDetailController {
	
	@Autowired
	private CompanyDetailService comDetailService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	/**新增企業會員**/
	@PostMapping(value = "/addCom")
	public String insertComDetail(@RequestPart("comRealname") String realname , 
			  					  @RequestPart("comPhonenumber") String phonenumber ,
			  					  @RequestPart("photo")MultipartFile userphoto ,
			  					  @RequestPart("comLocation") String location ,
			  					  @RequestPart("comlevel") String level , 
			  					  @RequestPart("userName") String userName ,
			  					  @RequestPart("userPwd") String userPwd , 
			  					  UserAccount addUserAccount , CompanyDetail addComDetail , Model model) {
		//塞CompanyDetail的值
		addComDetail.setRealname(realname);
		addComDetail.setPhonenumber(phonenumber);
		addComDetail.setLocation(location);
		addComDetail.setLevel(level);
		//塞圖片，需轉byte[]
		if (userphoto != null && !userphoto.isEmpty()) {
			try {
				byte[] b = userphoto.getBytes();
				addComDetail.setUserphoto(b);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("圖片發生異常: " + e.getMessage());
			}
		}
		//新增會員
		addUserAccount.setAccountIndex(userName);
		addUserAccount.setPassword(userPwd);
		//建立OneToOne連結
		addUserAccount.setCompanyDetail(addComDetail);
		Integer flag;
		try {
			flag = userAccountService.save(addUserAccount);
			if(flag ==1) {
				System.out.println("新增會員成功，新增ComDetail");
				CompanyDetail companyDetail = comDetailService.insert(addComDetail);
				model.addAttribute("comDetailId", companyDetail.getCompanyDetailId());
			}else if(flag==-1) {
				System.out.println("會員帳號重複，新增失敗");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "company/loginCompany";
	}
	
	/**修改會員-讀取要修改的會員資料傳到修改會員頁面**/
	@GetMapping(value = "/updateCom/{comId}")
	public String updateCompany(@PathVariable("comId") Integer comId , Model model) {
		CompanyDetail comDetail = comDetailService.findById(comId);
		System.out.println(comDetail.getLevel());
		System.out.println(comDetail.getLocation());
		System.out.println(comDetail.getPhonenumber());
		System.out.println(comDetail.getRealname());
		System.out.println(comDetail.getUserphoto());
//		System.out.println(comDetail.getUserAccount()); //為什麼取不到userAccount資料?
//		System.out.println(comDetail.getUserAccount().getAccountIndex());
//		System.out.println(comDetail.getUserAccount().getPassword());
		model.addAttribute("comDetail",	comDetail);
		return "company/updateCompany";
	}
	
	/**修改會員-接收修改後的資料**/
	@PostMapping("/updateCom/{comId}")
	public String modify( @ModelAttribute("comDetail") CompanyDetail comDetail, BindingResult result, Model model,
						  @PathVariable("comId") Integer comId, HttpServletRequest request) {
		System.out.println("--------- 收到修改資料 ---------");
		System.out.println(comDetail.getLevel());
		System.out.println(comDetail.getLocation());
		System.out.println(comDetail.getPhonenumber());
		System.out.println(comDetail.getRealname());
		System.out.println(comDetail.getUserphoto());
//		System.out.println(comDetail.getUserAccount().getAccountIndex());
		
		
		
		CompanyDetail  companyDetail = comDetailService.update(comDetail);
		System.out.println("新圖:"+companyDetail.getUserphoto());
			return "company/showAllCompany";
		}
	
}
