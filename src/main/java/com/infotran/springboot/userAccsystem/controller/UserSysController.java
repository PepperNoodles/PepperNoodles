package com.infotran.springboot.userAccsystem.controller;


import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.infotran.springboot.commonmodel.AuthUserDetails;
import com.infotran.springboot.commonmodel.CompanyDetail;
import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.FoodTagUser;
import com.infotran.springboot.commonmodel.FriendList;
import com.infotran.springboot.commonmodel.Like;
import com.infotran.springboot.commonmodel.MessageBox;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.Roles;
import com.infotran.springboot.commonmodel.SendEmail;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.commonmodel.UserDetail;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.loginsystem.dao.FoodTagRepository;
import com.infotran.springboot.loginsystem.dao.FoodTagUserRepository;
import com.infotran.springboot.loginsystem.service.UserAccountService;
import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.service.Impl.CancelOrderServiceImpl;
import com.infotran.springboot.shoppingmall.service.Impl.OrderListServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.FriendSysServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.LikeServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.MessageBoxServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.UserDetailServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.UserSysServiceImpl;
@SessionAttributes(names = {"userAccount","comDetail","rests","user"})
@Controller
public class UserSysController {


	String imageRootDirectory = "C:\\myfolder\\PPNpics";


	@Resource
	CancelOrderServiceImpl cancelorderservice;

    @Autowired
    private SessionRegistry sessionRegistry;

	
	File imageFolder = null; 
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	LikeServiceImpl likeServiceImpl;
	
	@Autowired
	SendEmail sendemail;
	
	@Autowired
	UserDetailServiceImpl uDetailService;
	
	@Autowired
	 UserAccountService service;
	
	@Autowired
	UserSysServiceImpl uSysServiceImpl;
	
	@Autowired
	FriendSysServiceImpl friendSysServiceImpl;
	
	@Autowired
	ServletContext servletContext;	
	
	@Autowired
	MessageBoxServiceImpl msnServiceImpl;
	
	@Autowired
	OrderListServiceImpl olistservice;
	
	@Autowired
	FoodTagRepository ftr;

	
	@Autowired
	 FoodTagRepository foodTagRepository;
	
	@Autowired
	FoodTagUserRepository foodTagUserRepository;
	
	@Autowired
	RestaurantService restaurantService;
	
	

	//??????Spring Security???????????????????????????????????????
    public void listLoggedInUsers() {
        final List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for(final Object principal : allPrincipals) {
            if(principal instanceof AuthUserDetails) {
                final AuthUserDetails user = (AuthUserDetails) principal;

                // Do something with user
                System.out.println("users online:====================================================" + 	user.getUsername());
            }
        }
    }
    
    
    //???Security???????????????session
    @GetMapping("/userPreLoggin/getName")
	@ResponseBody
	 public String userPreLoggin(@RequestParam(name = "username")String username,Model model) {

			UserAccount user = uSysServiceImpl.findByAccountIndex(username);	
			if (user != null) {
				
				if(user.getCompanyDetail() != null) {
					Set<Restaurant> rests = user.getRestaurant();
					CompanyDetail comDetail = user.getCompanyDetail();
					model.addAttribute("user", user);
					model.addAttribute("comDetail", comDetail);
					model.addAttribute("rests", rests);
				}else {
					model.addAttribute("userAccount", user);
				}
				
				
				return "true";
			}
	  
	   return "false";
	 }
    
	
	public UserSysController() {
		imageFolder = new File(imageRootDirectory, "images");
		if ( !imageFolder.exists() )
			imageFolder.mkdirs();
		
	}
	
	//???????????????????????????
	@GetMapping(value="/welcome")
	public String welcome(Model m) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		m.addAttribute("userAccount", user);
		
		return "index";
	}

	public String returnNamePath() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    System.out.println(currentUserName);
		    return currentUserName;
			}
		System.out.println("no logging user currently!!");
			return null;
	}
	
	//??????websocket
	@GetMapping("/user/websocket")
	public String userMain() {
		return "websocket/index";
	}
	
	
	//???Authentication??????????????????????????????(?????????????????????????????????????????????ONLOAD???
	public String returnRole() {
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		Set<Roles> roles = user.getRoles();
		String roleName="";
		
		for(Roles role:roles) {
			
			if(role.getRoleName()=="normal"){
				
				roleName= "normal";
				
			}else if(role.getRoleName()=="company"){
			
				roleName= "company";
				
			}else if(role.getRoleName()=="admin"){
				
				roleName= "admin";
				
			}else {
				
				System.out.println("no logging user currently!!");
				return null;
			}
		}
		return roleName;
	}
	

	
	//?????????????????????test
	@GetMapping(value="user/addNewComment" )
	public String addNewComment(Model model) {
		//test
		System.out.println(returnNamePath());

		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		model.addAttribute("userAccount", user);
		UserAccount commentUser = uSysServiceImpl.findByAccountIndex("chrislodouchebag@gmail.com");
		MessageBox msn = new MessageBox();
		msn.setText("Hello ?????????? ????????????~");
		msn.setTime(new Date());
		msn.setLikeAmount(0);
		msn.setNetizenAccount(commentUser);
		msn.setUserAccount(user);
		msn.setMessageBox(null);
		
		msnServiceImpl.save(msn);
		//???????????????user???msn
//		List<MessageBox> msns = user.getMsnBox();
//		msns.add(msn);
//		user.setMsnBox(msns);
//		uSysServiceImpl.update(user);
		return "userpage/usermain";
	}
	
	//???????????????????????????test
	@GetMapping(value="user/addNewReplyComment" )
	public String addNewReplyComment(Model model) {
		//test
		System.out.println(returnNamePath());

		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());

		model.addAttribute("userAccount", user);
		UserAccount commentedUser = uSysServiceImpl.findByAccountIndex("stevenzchao439@gmail.com");
		System.out.println(returnNamePath());
		MessageBox msn = new MessageBox();
		msn.setText("??????????????????????????????~~");
		msn.setTime(new Date());
		msn.setLikeAmount(0);
		msn.setNetizenAccount(commentedUser);
		msn.setUserAccount(user);
		msn.setMessageBox(msnServiceImpl.findById(2));
		msnServiceImpl.save(msn);
		return "userpage/usermain";
	}
	
	//????????????&?????????????????????test
	@GetMapping(value="user/deleteReplyComment" )
	public String deleteReplyComment(Model model) {
		//test
		MessageBox msn = msnServiceImpl.findById(2);
		msn.setNetizenAccount(null);
		msn.setUserAccount(null);
		msn.setReplyMessageBoxes(null);
		msnServiceImpl.delete(msn);
		return "userpage/usermain";
	}
	
	//????????????=============================================================================================================================

	//????????????????????????
	@GetMapping(value="/forgotPassword")
	public String forgotPassword() {

			return "ForgotPassword";
	}
	
	//?????????????????????????????????
	@PostMapping(value="/sendVerificationPassword")
	public  @ResponseBody Map<String,String> sendVerifiacationEmail(@RequestBody() String accountIndex ){
		Map<String,String> map = new HashMap<String, String>();

		UserAccount user2 = uSysServiceImpl.findByAccountIndex(accountIndex);
		String verificationpasswordcode = sendemail.getRandomPwd();
		System.out.println(verificationpasswordcode);
		System.out.println("user2========="+user2.getAccountIndex());
		
		String bcEncode1 = new BCryptPasswordEncoder().encode(verificationpasswordcode);
		user2.setPassword(bcEncode1);
		user2.setCode(verificationpasswordcode);
		uSysServiceImpl.update(user2);
		
		boolean emailResult = sendemail.sendEmail(user2);
		if (emailResult) {
			map.put("verificationpassword", "?????????????????????Email???????????????????????????");
		}
		return map;

	}	
	
//???????????????????????????======================================================================================================================

	@PostMapping(value="/user/changeBasicDetails")
	public @ResponseBody String changeBasicDetails( @RequestBody UserDetail userDetail,HttpSession ses,Model model) throws Exception{
		System.out.println("here i go againhere i go againhere i go againhere i go againhere i go againhere i go againhere i go againhere i go againhere i go again");
		String message ="";
		UserAccount useraccount1 =(UserAccount) ses.getAttribute("userAccount");
		UserDetail userDetailOG = useraccount1.getUserAccountDetail();
		userDetailOG.setBirthDay(userDetail.getBirthDay());
		userDetailOG.setNickName(userDetail.getNickName());
		userDetailOG.setRealName(userDetail.getRealName());
		userDetailOG.setPhoneNumber(userDetail.getPhoneNumber());
		userDetailOG.setLocation(userDetail.getLocation());
		userDetailOG.setGender(userDetail.getGender());
		useraccount1.setUserAccountDetail(userDetailOG);
		
		service.update(useraccount1);
		model.addAttribute("userAccount", useraccount1);
		message = "success";

		return message;
	}
	
	
	@GetMapping(value="/user/getFoodTagsAjax")
	public @ResponseBody List<FoodTag>  /* FoodTag Set<FoodTag> */showFoodTag( ) throws Exception{
		System.out.println("here i go for FOO FOO FOO TAG TAG TAG FOO FOO FOO TAG TAG TAGFOO FOO FOO TAG TAG TAGFOO FOO FOO TAG TAG TAGFOO FOO FOO TAG TAG TAGFOO FOO FOO TAG TAG TAG");
		List<FoodTag> foodtags = new ArrayList<>();		
//		FoodTag x =ftr.findByFoodTagIid(7);
//		System.out.println(x.getFoodTagName());
		int i=1;
		while(ftr.findById(i).isEmpty()==false) {
				System.out.println(ftr.findByFoodTagIid(i).getFoodTagName());
				foodtags.add(ftr.findByFoodTagIid(i));
				i++;
			}
		i=4;
		while(ftr.findById(i).isEmpty()==false) {
			System.out.println(ftr.findByFoodTagIid(i).getFoodTagName());
			foodtags.add(ftr.findByFoodTagIid(i));
			i++;
		}


		return foodtags;

	}
	
	
	@PostMapping(value="/user/confirmFoodTagsChangeAjax")
	public @ResponseBody Set<FoodTag> saveAccountInterest(
			@RequestBody String[] interest,
			Model model ){
		String message ="";
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		

		//??????????????????
		Set<FoodTagUser> usersetss =foodTagUserRepository.getByFkuserid(user);
		System.out.println(usersetss.size());
		for(FoodTagUser foodTagUser:usersetss) {
			foodTagUser.setFkuserid(null);
			foodTagUser.setFkfoodtagid(null);

			System.out.println("brfore delete=====================================!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			//?????????????????????
			foodTagUserRepository.deleteById(foodTagUser.getFooTagUserId());
			System.out.println("after delete=====================================!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			message+= "????????????";
		}

		Set<FoodTagUser> userSet = user.getUserTags();
		userSet.clear();
		user.setUserTags(userSet);
		uSysServiceImpl.update(user);

		System.out.println("userSet is empty: " + userSet.isEmpty());
		
		Session session = (Session) em.getDelegate();
		session.clear();
			for(int i = 0; i<interest.length; i++) {
				System.out.println("json??????server??????" + interest[i]);
				String tagName = interest[i];
				//???json?????????????????????DB??? FoodTag
				FoodTag tag = foodTagRepository.findByFoodTagName(tagName);
					
				//???????????????????????????
				FoodTagUser ftu = new FoodTagUser();
				//???????????????????????????
				ftu.setFkfoodtagid(tag);
				ftu.setFkuserid(user);
				foodTagUserRepository.saveAndFlush(ftu);


//				userSet.add(ftu);
//				Set<FoodTagUser> tagSet =  tag.getFoodTagUsers();
//				tagSet.add(ftu);
//				tag.setFoodTagUsers(tagSet);
//				foodTagRepository.saveAndFlush(tag);
				System.out.println("save foodtag");
//				tag.setFoodTagUsers(tagSet);
//				userSet.add(ftu);
//				user.setUserTags(userSet);
//				uSysServiceImpl.update(user);
				message+= "????????????";
			}

			Set<FoodTag> userSetFake = new HashSet<>();
			for(String x:interest) {
				FoodTag y = foodTagRepository.findByFoodTagName(x);
				userSetFake.add(y);
			}
			//			 Set<FoodTag> z = new HashSet<>();
//			List<FoodTag> x = foodTagRepository.findAll();
//			for(FoodTag y: x) {
//				if(y.getFoodTagUsers().contains(user)) {
//					z.add(y);
//				}
//			}


//			System.out.println("add user1 in=============********************========================!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//			for(FoodTagUser ft:user1.getUserTags()) {
//				System.out.println(ft.getFkfoodtagid().getFoodTagName());
//			}
			
System.out.println("WEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!!!!!");

			for(FoodTag x:userSetFake) {
				String y = x.getFoodTagName();
				System.out.println(y);
			}

			UserAccount user1 = uSysServiceImpl.findByAccountIndex(returnNamePath());
			model.addAttribute("userAccount", user1);

			
		return userSetFake;
	}
	
	
	
	@PostMapping(value="/user/confirmPhotoChangeAjax",consumes={"multipart/mixed","multipart/form-data","application/json"})
	public @ResponseBody String changePhoto( 
			@RequestPart("file")MultipartFile file) throws Exception{
		String message = "";
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		UserDetail userDetail = user.getUserAccountDetail();
		
				
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
				throw new RuntimeException("????????????????????????: " + e.getMessage());
			}
		}

		user.setUserAccountDetail(userDetail);
		
			service.update(user); 
				message = "success";

		try {
			File fileplace = new File(imageFolder, "UserImage_" + user.getAccountIndex() + extFilename);
			file.transferTo(fileplace);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("????????????????????????: " + e.getMessage());
		}
		return message;
	}
	
	@GetMapping(value="/user/replacePhotoAjax")
	public @ResponseBody Integer replacephoto( ) throws Exception{
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		return user.getUserAccountDetail().getUseretailId();
	}
	
//======================================================================================================================

	//normaluser buffer??????MVC?????????
	@PostMapping(value="user/loginMVC" )
	public String gopage(Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		System.out.println(returnNamePath());
		model.addAttribute("userAccount", user);
		return "userpage/usermain";
	}
	
	//???????????????????????????ajax??????
	@GetMapping(value="/user/showAllCommentAjax" ,produces= "application/json")
	@ResponseBody
	public List<MessageBox> showAllComments(Model model,@ModelAttribute("userAccount")UserAccount useraccount) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
//		List<MessageBox> userMsn = msnServiceImpl.findByUserAccount(user);
		List<MessageBox> userMsn = user.getMsnBox();
		Hibernate.initialize(userMsn);
		List<MessageBox> userMsnNull = new ArrayList<MessageBox>();
		for(int i =0; i<userMsn.size(); i++) {
			if(userMsn.get(i).getMessageBox()==null) {
				userMsnNull.add(userMsn.get(i));
//				Hibernate.initialize(userMsn.get(i));
//				System.out.println(userMsn.get(i).getMessageBox().getUserMessageId());
//				for(int j =0;j<userMsn.get(i).getLikes().size();j++) {
//					System.out.println("logic DOn DONDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
//					Hibernate.initialize(userMsn.get(i).getLikes().get(j).getFkUserAccountId());
//
//					System.out.println(userMsn.get(i).getLikes().get(j).getFkUserAccountId());
//				}
			}	
		}
		System.out.println("????????????");
		return userMsnNull;
	}
	
	//???????????????????????????ajax??????
	@GetMapping(value="/user/showOtherPageAllCommentAjax" ,produces= "application/json")
	@ResponseBody
	public List<MessageBox> showOtherPageAllComments(@RequestParam(value = "viewUserAccount") String accountIndex) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(accountIndex);
//		List<MessageBox> userMsn = msnServiceImpl.findByUserAccount(user);
		List<MessageBox> userMsn = user.getMsnBox();
		Hibernate.initialize(userMsn);

		List<MessageBox> userMsnNull = new ArrayList<MessageBox>();
		for(int i =0; i<userMsn.size(); i++) {
			if(userMsn.get(i).getMessageBox()==null) {
				userMsnNull.add(userMsn.get(i));
				
			}	
		}
		return userMsnNull;
	}
	
	//?????????????????????????????????ajax
	@GetMapping(value="user/addNewCommentAjax" )
	@ResponseBody 
	public String addNewCommentAjax(@RequestParam(value = "comment") String comment
			/*,@RequestParam(value = "useraccount")String useraccount*/)
	{
		System.out.println("Hi I'm Here");
		String status ="";
		UserAccount commenter = uSysServiceImpl.findByAccountIndex(returnNamePath());
		MessageBox newMSN = new MessageBox();
		newMSN.setText(comment);
		newMSN.setTime(new Date());
		newMSN.setLikeAmount(0);
		newMSN.setNetizenAccount(commenter);
		newMSN.setUserAccount(commenter);
		Integer save =msnServiceImpl.save(newMSN);
		if(save==1) {
			status="???????????????";
		}else {
			status="??????????????????";
		}
		return status;
	}
	
	//?????????????????????????????????ajax
	@GetMapping(value="user/addNewOtherCommentAjax" )
	@ResponseBody 
	public String addOtherPageNewCommentAjax(@RequestParam(value = "comment") String comment
			,@RequestParam(value = "viewUserAccount")String useraccount)
	{
		System.out.println("Hi I'm Here for adding new other page's comment");
		String status ="";
		UserAccount viewUserAccount = uSysServiceImpl.findByAccountIndex(useraccount);
		UserAccount commenter = uSysServiceImpl.findByAccountIndex(returnNamePath());
		MessageBox newMSN = new MessageBox();
		newMSN.setText(comment);
		newMSN.setTime(new Date());
		newMSN.setLikeAmount(0);
		newMSN.setNetizenAccount(commenter);
		newMSN.setUserAccount(viewUserAccount);
		Integer save =msnServiceImpl.save(newMSN);
		if(save==1) {
			status="???????????????";
		}else {
			status="??????????????????";
		}
		return status;
	}
	
	//?????????????????????Ajax??????
	@PostMapping(value="/user/addNewReplyCommentAjax/{id}", consumes= {"application/json"})
	@ResponseBody
	public String addNewReplyComment(@PathVariable(value="id") Integer id, @RequestBody MessageBox replyMSN) {
		System.out.println("");
		String message = "";
		MessageBox msn = new MessageBox();
		System.out.println("======================================="+replyMSN.getText());
		msn.setText(replyMSN.getText());
		msn.setTime(new Date());
		msn.setLikeAmount(0);
		//??????????????????????????????
		MessageBox userMsn = msnServiceImpl.findById(id);
		//???????????????????????????
		UserAccount userAccount = uSysServiceImpl.findByAccountIndex(userMsn.getUserAccount().getAccountIndex());
		msn.setUserAccount(userAccount);
		//???????????????
		msn.setNetizenAccount(uSysServiceImpl.findByAccountIndex(returnNamePath()));
		msn.setMessageBox(userMsn);
//		userMsn.getReplyMessageBoxes().add(msn);
		Integer success =msnServiceImpl.save(msn);
		if(success==1) {
			message="??????????????????";
		}else {
			message="??????????????????";
		}
		return message;
	}
	
	
	//???????????????Ajax??????
	@GetMapping(value="user/deleteCommentAjax" )
	@ResponseBody
	public String deleteMainComment(@RequestParam(value = "id") Integer id) {
		String message = "";
		UserAccount commenter = uSysServiceImpl.findByAccountIndex(returnNamePath());
		MessageBox msn = msnServiceImpl.findById(id);
		if(commenter.getAccountId()!=msn.getNetizenAccount().getAccountId()){
			message = "???????????????????????????~";
			return  message;
		}else{
			msn.setNetizenAccount(null);
			msn.setUserAccount(null);
			List<MessageBox> listMsn = msn.getReplyMessageBoxes();
			List<Integer> messageIdToDelete = new ArrayList<>();
			if(listMsn!=null) {
				for(MessageBox otherMSN :listMsn ) {
					otherMSN.setMessageBox(null);
					otherMSN.setNetizenAccount(null);
					otherMSN.setUserAccount(null);
					messageIdToDelete.add(otherMSN.getUserMessageId());
					msnServiceImpl.delete(otherMSN);
//					msnServiceImpl.save(otherMSN);
				}
			}
			Integer success =msnServiceImpl.delete(msn);
			if(success==1) {
				message="??????????????????";
			}else {
				message="??????????????????";
			}
		}
		return message;
	}
	
	//Update ???????????????Ajax??????
	@PostMapping(value="/user/updateCommentAjax/", consumes= {"application/json"})
	@ResponseBody
	public String updateMainComment( @RequestBody MessageBox updateMSN) {
		String message = "";
		UserAccount talker = uSysServiceImpl.
				findByAccountIndex( msnServiceImpl.findById(updateMSN.getUserMessageId()).
				getNetizenAccount().getAccountIndex());
		UserAccount loginner = uSysServiceImpl.findByAccountIndex(returnNamePath());
		if(loginner.getAccountId()!=talker.getAccountId()){
			message = "???????????????????????????!";
			return message;
		}else {
			MessageBox msn = msnServiceImpl.findById(updateMSN.getUserMessageId());
			System.out.println("======================================="+updateMSN.getText());
			msn.setText(updateMSN.getText());
			msn.setTime(new Date());
			msn.setLikeAmount(updateMSN.getLikeAmount());
			Integer success =msnServiceImpl.save(msn);
			if(success==1) {
				message="??????????????????";
			}else {
				message="??????????????????";
			}
		}
		return message;
	}
	
	//?????????????????????
	@GetMapping(value="/user/updateLikeCommentAjax" )
	@ResponseBody
	public String updateLikeComment( @RequestParam(value = "id") Integer msnID) {
		String message = null;
		MessageBox msn = msnServiceImpl.findById(msnID);
		System.out.println("=======================================???????????????????????????~~~~~~~~~~~~~~~~~~~~~~"+msn.getText());
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		Integer flag =likeServiceImpl.save(user, msn);
		
		if(flag==1) {
			msn.setLikeAmount(msn.getLikeAmount()+1);
			Integer success =msnServiceImpl.save(msn);
			if(success==1) {
				message=  String.valueOf(msn.getLikeAmount());
			}else {
				message=String.valueOf(msn.getLikeAmount());

			}
		}else {
			message=String.valueOf(msn.getLikeAmount());
			System.out.println("?????????????????????????????????????????????~~~!!!!!");
		}
		return message;
	}
	
	//???????????????????????????
	@GetMapping(value="/user/updateDisLikeCommentAjax" )
	@ResponseBody
	public String updateDisLikeComment( @RequestParam(value = "id") Integer msnID) {
		String message = null;
		MessageBox msn = msnServiceImpl.findById(msnID);
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		System.out.println("=======================================?????????????????????????????????????????????~~~~~~~~~~~~~~~~~~~~~~"+msn.getText());
		Integer delete =likeServiceImpl.delete(user, msn);
		if(delete==1) {
			msn.setLikeAmount(msn.getLikeAmount()-1);
			Integer success =msnServiceImpl.save(msn);
			System.out.println("????????????????????????MSNBOX: " + success );
		}
			message=  String.valueOf(msn.getLikeAmount());
		return message;
	}
	
	//?????????????????????????????????
	@GetMapping(value="/user/showWhoLikeAjax" )
	@ResponseBody
	public List<UserAccount> showWhoLikeCommentAjax( @RequestParam(name = "msnID") Integer msnID) {
		String message = null;
		MessageBox msn = msnServiceImpl.findById(msnID);
		List<Like> msnLikes = msn.getLikes();
		List<UserAccount> preToSend = new ArrayList<UserAccount>();
		for(int i=0;i<msnLikes.size();i++) {
			preToSend.add(msnLikes.get(i).getFkUAId());
		}
		return preToSend;
	}
	
//??????????????????======================================================================================================================
	
	//?????????????????????????????????
	@GetMapping(value="/user/checkRestaurantCollection" )
	@ResponseBody
	public String checkRestaurantCollection( @RequestParam(name = "resID") Integer resID) {
		String message = null;
		Restaurant res = restaurantService.get(resID);
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		List<Restaurant> userCollections = user.getRestaurantCollections();
		if(userCollections.contains(res)) {
			System.out.println("???????????????????????????????????????!");
			message = "true";
		}else {
			user.setRestaurantCollections(userCollections);
			System.out.println("?????????????????????~~");
			message="???????????????~~";
		}
		return message;
	}
	
	
	//???????????????????????????????????????
	@GetMapping(value="/user/addRestaurantCollection" )
	@ResponseBody
	public String addRestaurantCollection( @RequestParam(name = "resID") Integer resID) {
		String message = null;
		Restaurant res = restaurantService.get(resID);
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		List<Restaurant> userCollections = user.getRestaurantCollections();
		if(userCollections.contains(res)) {
			System.out.println("???????????????????????????????????????!,??????????????????");
			
			for(int i=0; i<userCollections.size();i++) {
				System.out.println(userCollections.get(i).getRestaurantId() + " " + userCollections.get(i).getRestaurantName());
				if(userCollections.get(i).getRestaurantId()==res.getRestaurantId()) {
					userCollections.remove(i);
					user.setRestaurantCollections(userCollections);
				}
			}
			uSysServiceImpl.update(user);
			message = "????????????";
		}else {
			userCollections.add(res);
			user.setRestaurantCollections(userCollections);
			service.update(user);
			message="success";
		}
		return message;
	}
	
	//?????????????????????????????????
	@GetMapping(value="/user/showUserCollections" )
	@ResponseBody
	public List<Restaurant> showUserCollections( ) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		List<Restaurant> userCollections = user.getRestaurantCollections();
		return userCollections;
	}
	
	//?????????????????????????????????
	@GetMapping(value="/user/showOtherUserCollections" )
	@ResponseBody
	public List<Restaurant> showOthersUserCollections(@RequestParam(value = "viewUserAccount") String accountIndex ) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(accountIndex);
		System.out.println("???????????????: " + accountIndex);
		Hibernate.initialize(user);
		List<Restaurant> userCollections = user.getRestaurantCollections();
		
		return userCollections;
	}
	
//======================================================================================================================
	
	@GetMapping(path="finduser")
	@ResponseBody
	public UserAccount finduser(@RequestParam("userAccountIndex") String mainAccountIndex,Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(mainAccountIndex);	
		//Hibernate.initialize(user);
		return user;
	}
	//???Authentication???????????????????????????????????????
	@GetMapping("/userLoggin/getName")
	@ResponseBody
	 public String returnNamePath(Model model) {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	  String currentUserName="";
	  if (!(authentication instanceof AnonymousAuthenticationToken)) {
	      currentUserName = authentication.getName();
	      System.out.println(currentUserName);
			UserAccount user = uSysServiceImpl.findByAccountIndex(currentUserName);	
			System.out.println(user.getAccountIndex());
			model.addAttribute("userAccount", user);
			listLoggedInUsers() ;
	      return currentUserName;
	   }
	  System.out.println("no logging user currently!!");
	   return null;
	 }
	
	//????????????user
	@GetMapping("/usercheck/{userAccountIndex}")
	@ResponseBody
	public UserAccount findUserd(@PathVariable("userAccountIndex") String mainAccountIndex){
		UserAccount user = uSysServiceImpl.findByAccountIndex(mainAccountIndex);	
		return user;

	}
	
	//??????mainUser???friendList
	@GetMapping(path="findMainfriend/{userAccountIndex}")
	@ResponseBody
	public List<UserAccount> findMainUserFriend(@PathVariable("userAccountIndex") String mainAccountIndex){
		UserAccount user = uSysServiceImpl.findByAccountIndex(mainAccountIndex);
		List<FriendList> mainUserFriends = friendSysServiceImpl.findFriendByUserAndRelation(user,"Y");
//		Hibernate.initialize(user.getFriends());
//		Set<FriendList> mainUserFriends = user.getFriends();
		List<UserAccount> myFriend = new ArrayList<UserAccount>();
		if (mainUserFriends.isEmpty()==false) {
			for(FriendList flist:mainUserFriends) {
				UserAccount myFriendList = flist.getFriends();
				//System.out.println(myFriendList.getAccountIndex());
				myFriend.add(myFriendList);
				}
			return myFriend;
		}else {
			return null;
		}
	}
	
	//????????????(????????????sessionAttribute,userAccount,with session,?????????????????????),???????????????viewUser????????????????????????
	@GetMapping(path="addfriend/{userAccountIndex}/{viewAccountIndex}")
	@ResponseBody
	public String requestFriend(@PathVariable("userAccountIndex") String mainAccountIndex,@PathVariable("viewAccountIndex") String friendAccountIndex,Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(mainAccountIndex);
		UserAccount friend = uSysServiceImpl.findByAccountIndex(friendAccountIndex);
		List<FriendList> findflist = friendSysServiceImpl.findByFriendship(user, friend,"W");
		if (findflist.size()==0) {
			FriendList flist=new FriendList();
			flist.setMainUser(user);
			flist.setFriends(friend);
			flist.setFriendship("W");	
			friendSysServiceImpl.save(flist);
			return "request is sending";
		}
		
		
		return "request is already send";

		//return "request is sending";
	}
	
	//???postman???????????????????????????,????????????button??????(?????????session,????????????friendIndex),??????????????????viewUser?????????,??????????????????????????????
	@GetMapping(path="judgeFriendShip")
	@ResponseBody
	public String judgeFriendship(@RequestParam(name="userIndex") String userIndex,@RequestParam(name="friendIndex") String friendIndex) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(userIndex);
		UserAccount friend = uSysServiceImpl.findByAccountIndex(friendIndex);
		String result="";
		List<FriendList> flist=friendSysServiceImpl.findRelationFromUserAndFriend(user, friend);
		if (flist.size()>0) {
			FriendList friendship = flist.get(0);
			String relation = friendship.getFriendship();
			System.out.println("friendship=========:"+relation);
			if ("Y".equals(relation)) {
				result="friend";
			}else if ("W".equals(relation)) {
				result="wait_check";
				
			}else if ("N".equals(relation)) {
				result="reject";
			}else {
			}			
		}else 
			result="noFriendShip";		
		return result;
	}
	
	
	//???postMap?????????user???,???user????????????(?????????userIndex????????????)===?????????,???????????????????????????????????????????????????
	@GetMapping(path="MainUserAddFriendwithIndex/{mainUserAccountIndex}/{userAccountIndex}")
	@ResponseBody
	public String checkFriend(@PathVariable("mainUserAccountIndex") String mainAccIndx,@PathVariable("userAccountIndex") String uAccIndx,Model model) {
		//?????????model??????????????????
		UserAccount user =uSysServiceImpl.findByAccountIndex(mainAccIndx);		
		UserAccount friend = uSysServiceImpl.findByAccountIndex(uAccIndx);		
		//??????????????????
		FriendList flist=new FriendList();
		flist.setMainUser(user);
		flist.setFriends(friend);
		flist.setFriendship("Y");		
		friendSysServiceImpl.save(flist);
		
		//???waitCheck??????Yes
		List<FriendList> flistOld = friendSysServiceImpl.findByFriendship(friend, user, "W");
		FriendList flist0 = flistOld.get(0);
		System.out.println("=========getListLength======="+flistOld);
		flist0.setFriendship("Y");
		friendSysServiceImpl.save(flist0);
		
		return "add finish";
	}
	
	//??????main user???????????????????????????(????????????)
	@GetMapping(path="findMainUserFriendRequest")
	@ResponseBody
	public List<UserAccount> requestPerson(@RequestParam(name="mainUserIdx") String userIdx){
		UserAccount user = uSysServiceImpl.findByAccountIndex(userIdx);
		
		List<FriendList> friend = friendSysServiceImpl.findFriendByUserAndRelation(user,"W");
		List<UserAccount> requestUsers = new ArrayList<>();
		for(FriendList flist:friend) {
			UserAccount requestUser = flist.getFriends();
			requestUsers.add(requestUser);			}
		
		return requestUsers;
	}
	
	//main????????????????????????main,??????????????????????????????????????????user????????????
	@GetMapping(path="/findWhoWannaJoinMe")
	@ResponseBody
	public List<UserAccount> findWhoWannaJoinMe(@RequestParam(name="mainUserIdx") String userIdx){
		UserAccount user = uSysServiceImpl.findByAccountIndex(userIdx);
		List<FriendList> friend = friendSysServiceImpl.findFriendByFriendAndRelation(user,"W");
		List<UserAccount> requestUsers = new ArrayList<>();
		for(FriendList flist:friend) {
			UserAccount requestUser = flist.getMainUser();
			requestUsers.add(requestUser);
			}		
		return requestUsers;
		
	}
	
	//?????????session??????(????????????pathVariable????????????)
	@GetMapping(path="user/login/{userAccountIndex}")
	public String goViewWithSession(@PathVariable("userAccountIndex") String uAccIndx,Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(uAccIndx);		
		model.addAttribute("userAccount", user);
		System.out.println(returnNamePath());
		return "userpage/usermain";
	}
	
	//????????????
	@GetMapping(path="/user/login")
	public String goUserMainSession(Model model) {
		return "userpage/usermain";
	}
	
	//????????????????????????user,??????user???detail??????model,???????????????page
	@GetMapping(path="/userView/{userAccountIndex}")
	public String goUserView(@PathVariable("userAccountIndex") String uAccIndx,Model model ) {
		UserAccount currentUser = (UserAccount)model.getAttribute("userAccount");
		if (currentUser!=null && !(currentUser.getAccountIndex().equals(uAccIndx))) {
			UserAccount userAcc = uSysServiceImpl.findByAccountIndex(uAccIndx);
			if(userAcc!=null) {
				System.out.println(returnNamePath());

				UserDetail userAccDetail = userAcc.getUserAccountDetail();
				model.addAttribute("viewUserAccount",userAcc);
				model.addAttribute("viewUserAccountDetail", userAccDetail);
				return "userpage/viewuser";
			}
			return "userpage/viewuser";
		}else {
			return "userpage/usermain";
		}
				
		
		
	}
	
	//???Nickname???user(?????????????????????)
	@GetMapping(path="searchUserByNick",produces="application/json")
	@ResponseBody
	public List<UserAccount> searchUserNickName(@RequestParam(name="name") String name,Model model){
//		UserAccount currentUser = (UserAccount)model.getAttribute("userAccount");
		UserAccount currentUser = uSysServiceImpl.findByAccountIndex(returnNamePath());
		System.out.println("currentUser======="+currentUser.getAccountIndex());
		//????????????index??????
		String currentIndx = currentUser.getAccountIndex();
		System.out.println("currentUser======="+currentIndx);
		
		List<UserDetail> friendsDetail = uDetailService.finbyByNickName(name);
		List<UserAccount> friends=new ArrayList<UserAccount>();
				
		for(UserDetail udetail:friendsDetail) {				
			UserAccount userAcc=udetail.getUserAccount();		
			if(!(userAcc.getAccountIndex().equals(currentIndx)))
				friends.add(userAcc);
			}
		return friends;
	}
	
	
	//???detail id??????json
	@GetMapping(path = "/userDetail",produces="application/json")
	@ResponseBody
	public UserDetail getUserDetailById(@RequestParam(name="id") Integer detailId) {
		return uDetailService.get(detailId);
	}
	
	//???userIndex????????????user??????,?????????json
	@GetMapping(path="/user/{userAccountIndex}")
	@ResponseBody
	public UserAccount getUserByIndex(@PathVariable("userAccountIndex") String accIndex) {
		return uSysServiceImpl.findByAccountIndex(accIndex);
	}
	
	//???id??????database user??????
	@GetMapping(path="/userProtrait/{userDetailID}",produces = "image/jpeg")
	public ResponseEntity<byte[]> getUserDetailProtrait(@PathVariable("userDetailID") Integer detailId) {
		UserDetail uDtail = uDetailService.get(detailId);
		Blob photo=null;
		byte[] uPhoto=null;
		photo = uDtail.getUserPhoto();
		if(photo!=null) {	 		
			try {
				int blobLength = (int) photo.length();  
				uPhoto = photo.getBytes(1, blobLength);
				photo.free();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		else {
			String path = servletContext.getRealPath("/");
			path+="images/NoImage/NoImage_Male.png";
			System.out.println(path);
			File noimage = new File(path);
			try {
				uPhoto=FileUtils.readFileToByteArray(noimage);				
				} catch (IOException e) {e.printStackTrace();}
		}		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(uPhoto,headers,HttpStatus.OK);		
	}
	
	@GetMapping(value="/user/getOrderList")
	public @ResponseBody Map<String,ArrayList<OrderList>> findUserOrderListByUserName(@ModelAttribute("userAccount")UserAccount user){
		Map<String,ArrayList<OrderList>> mapview = new HashMap<String,ArrayList<OrderList>>();
		ArrayList<OrderList>  orderViewList = olistservice.findOrderList(user.getAccountId());
		mapview.put("AccountMemberOrderList", orderViewList);
		return mapview;
	}
	
	@GetMapping(value="/user/getDetailForEachOrder")
	public @ResponseBody Map<String,Object> findDetailForOrderList(@RequestParam("orderid")Integer orderid){
		ArrayList<OrderDetail> detaillist = olistservice.findOrderDetailByFkOrderId(orderid);
		List<String> pname = new ArrayList<String>();
		List<Integer> pprice = new ArrayList<Integer>();
		List<Integer> pamount = new ArrayList<Integer>();
		for (OrderDetail od : detaillist) {
			String name = od.getProduct().getProductName();
			Integer price = od.getProduct().getProductPrice();
			Integer amount = od.getAmount();
			pname.add(name);
			pprice.add(price);
			pamount.add(amount);
		}
		Map<String,Object> mapview = new HashMap<String,Object>();
		mapview.put("pname", pname);
		mapview.put("pprice", pprice);
		mapview.put("pamount", pamount);
		return mapview;
	}
	
	
}
