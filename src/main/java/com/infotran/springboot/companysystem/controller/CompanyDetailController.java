package com.infotran.springboot.companysystem.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import com.infotran.springboot.commonmodel.Roles;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.CompanyDetailService;
import com.infotran.springboot.loginsystem.service.RolesService;
import com.infotran.springboot.loginsystem.service.UserAccountService;

@Controller
@SessionAttributes(names = {"comDetail","comDetailId"})
public class CompanyDetailController {
	

	String NoCompany = "/images/NoImage/NoCompany.png";

	@Autowired
	private RolesService rolesService;
	
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
		//幫密碼加密
		String bcEncode1 = new BCryptPasswordEncoder().encode(addUserAccount.getPassword());
		addUserAccount.setPassword(bcEncode1);
		//建立OneToOne連結
		addUserAccount.setCompanyDetail(addComDetail);
		Integer flag;
		
		//建立帳號的企業權限
		Roles userRole = rolesService.findById(2);
		Set<Roles> userRoles =addUserAccount.getRoles();
		userRoles.add(userRole);
		addUserAccount.setRoles(userRoles);
		
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
	/**修改會員密碼-讀取要修改的會員資料傳到修改會員頁面**/
	@GetMapping(value = {"/updateComPwd/{comId}"} )
	public String updateComPwd(@PathVariable("comId") Integer comId , CompanyDetail comDetail , Model model) {
		comDetail = comDetailService.findById(comId);
		model.addAttribute("comDetail",	comDetail);
		return "company/updatePassword";
	}
	
	/**修改會員密碼-接收密碼&新密碼**/
	@PostMapping(value = "/updatePwd/{comId}",produces="application/json")
	public  @ResponseBody Map<String,String> nextComPwd(@PathVariable("comId") Integer comId ,
														@RequestParam("userPwd") String userPwd,
														@RequestParam("nextUserPwd") String nextUserPwd,
														Model model) {
		System.out.println(comId+":"+userPwd+":"+nextUserPwd);
		
		Map<String,String> map = new HashMap<String, String>();
		CompanyDetail  companyDetail = comDetailService.findById(comId);
		UserAccount user = companyDetail.getUserAccount();
		//查询舊密码
		String pwd = user.getPassword();
		System.out.println("原始密碼:"+pwd);
		
		//encode.matches比对密码是否相等(輸入的密碼<未加密>,GET出的舊密碼<加密的>)
		BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
		if (encode.matches(userPwd, pwd)) {
	        String bcryptPasswd = (encode.encode(nextUserPwd));
			user.setPassword(bcryptPasswd);
			userAccountService.update(user);
			map.put("success", "修改密碼成功!");
	    }
		
		else {
			map.put("failure", "密碼錯誤，修改失敗!");
		}
		return map;
	}
	
	/**修改會員-讀取要修改的會員資料傳到修改會員頁面**/
	@GetMapping(value = {"/updateCom/{comId}"} )
	public String updateCompany(@PathVariable("comId") Integer comId , Model model , CompanyDetail comDetail) {
		comDetail = comDetailService.findById(comId);
		model.addAttribute("comDetail",	comDetail);
		return "company/updateCompany";
	}
	
	/**修改會員-接收修改後的資料**/
	@PostMapping("/updateCom/{comId}")
	public String modify( @ModelAttribute("comDetail") CompanyDetail comDetail, BindingResult result, Model model,
						  @RequestPart("userphoto")MultipartFile userphoto ,
						  @PathVariable("comId") Integer comId, HttpServletRequest request) {
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
			return "company/showCompany";
		}
	
	/**秀圖片**/
	@GetMapping("/getComPicture/{id}")
	public ResponseEntity<byte[]> getComPicture(@PathVariable("id") Integer id) {
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
