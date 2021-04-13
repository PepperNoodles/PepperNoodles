package com.infotran.springboot.companysystem.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@SessionAttributes(names = {"comDetail","comDetailId"})
public class CompanyDetailController {
	
	String NoCompany = "/images/NoImage/NoCompany.png";
	
	@Autowired
	private CompanyDetailService comDetailService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	ServletContext context;
	
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
		model.addAttribute("comDetail",	comDetail);
		return "company/updateCompany";
	}
	
	/**修改會員-接收修改後的資料**/
	@PostMapping("/updateCom/{comId}")
	public String modify( @ModelAttribute("comDetail") CompanyDetail comDetail, BindingResult result, Model model,
						  @RequestPart("userphoto")MultipartFile userphoto ,
						  @PathVariable("comId") Integer comId, HttpServletRequest request) {
		System.out.println("--------- 收到修改資料 ---------");
		System.out.println(comDetail.getLevel());
		System.out.println(comDetail.getLocation());
		System.out.println(comDetail.getPhonenumber());
		System.out.println(comDetail.getRealname());
		System.out.println(comDetail.getUserphoto());
//		System.out.println(comDetail.getUserAccount().getAccountIndex());
		
		if (userphoto != null && !userphoto.isEmpty()) {
			try {
				byte[] b = userphoto.getBytes();
				comDetail.setUserphoto(b);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("圖片發生異常: " + e.getMessage());
			}
		}
		
		CompanyDetail  companyDetail = comDetailService.update(comDetail); //comDetail 要是SESSION不然會變新增
		System.out.println("新圖:"+companyDetail.getUserphoto());
			return "company/showAllCompany";
		}
	
	
	@GetMapping("/picture/{id}")
	public ResponseEntity<byte[]> getPicture(@PathVariable("id") Integer id) {
		ResponseEntity<byte[]> re = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		CompanyDetail companyDetail = comDetailService.findById(id);
		if (companyDetail == null) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
	
		byte[] comImg = companyDetail.getUserphoto();
		if (comImg == null) {
			comImg = fileToByteArray(NoCompany);
		}
		re = new ResponseEntity<byte[]>(comImg, headers, HttpStatus.OK);

		return re;
	}

	private byte[] fileToByteArray(String NoCompany) {
		byte[] result = null;
		try (InputStream is = context.getResourceAsStream(NoCompany);
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
