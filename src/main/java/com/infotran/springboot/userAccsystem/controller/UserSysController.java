package com.infotran.springboot.userAccsystem.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.infotran.springboot.commonmodel.FriendList;
import com.infotran.springboot.commonmodel.MessageBox;
import com.infotran.springboot.commonmodel.Roles;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.commonmodel.UserDetail;
import com.infotran.springboot.loginsystem.service.UserAccountService;
import com.infotran.springboot.userAccsystem.service.inplement.FriendSysServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.MessageBoxServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.UserDetailServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.UserSysServiceImpl;

@SessionAttributes(names = "userAccount")
@Controller
public class UserSysController {
	
	@Autowired
	UserDetailServiceImpl uDetailService;
	
	@Autowired
	UserSysServiceImpl uSysServiceImpl;
	
	@Autowired
	FriendSysServiceImpl friendSysServiceImpl;
	
	@Autowired
	ServletContext servletContext;	
	
	@Autowired
	MessageBoxServiceImpl msnServiceImpl;
	
	
	//MessageBox 新增訊息的方法
	@PostMapping(value = "/saveMessageBox")
	public String saveMessageBox(){
		
		return "personalPage";
	}
	

	//從Authentication取得登入者的名字字串的方法
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
	
	
	//從Authentication取得登入者腳色的方法(但應該用不到ㄌ因為前端可以直接ONLOAD用
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
	
	//新增留言的方法test
	@GetMapping(value="user/addNewComment" )
	public String addNewComment(Model model) {
		//test
		System.out.println(returnNamePath());

		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		model.addAttribute("userAccount", user);
		UserAccount commentUser = uSysServiceImpl.findByAccountIndex("chrislodouchebag@gmail.com");
		MessageBox msn = new MessageBox();
		msn.setText("Hello 你好嗎? 衷心再見~");
		msn.setTime(new Date());
		msn.setLikeAmount(0);
		msn.setNetizenAccount(commentUser);
		msn.setUserAccount(user);
		msn.setMessageBox(null);
		
		msnServiceImpl.save(msn);
		//加入被留言user的msn
//		List<MessageBox> msns = user.getMsnBox();
//		msns.add(msn);
//		user.setMsnBox(msns);
//		uSysServiceImpl.update(user);
		return "userpage/usermain";
	}
	
	//新增回覆留言的方法test
	@GetMapping(value="user/addNewReplyComment" )
	public String addNewReplyComment(Model model) {
		//test
		System.out.println(returnNamePath());

		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());

		model.addAttribute("userAccount", user);
		UserAccount commentedUser = uSysServiceImpl.findByAccountIndex("stevenzchao439@gmail.com");
		System.out.println(returnNamePath());
		MessageBox msn = new MessageBox();
		msn.setText("珍重再見，期待再相逢~~");
		msn.setTime(new Date());
		msn.setLikeAmount(0);
		msn.setNetizenAccount(commentedUser);
		msn.setUserAccount(user);
		msn.setMessageBox(msnServiceImpl.findById(2));
		msnServiceImpl.save(msn);
		return "userpage/usermain";
	}
	
	//刪除留言、回覆留言的方法test
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
	
//======================================================================================================================

	//normaluser buffer頁面MVC登入法
	@PostMapping(value="user/loginMVC" )
	public String gopage(Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
		System.out.println(returnNamePath());
//		System.out.println(user.getMsnBox().get(0));		

		model.addAttribute("userAccount", user);

		return "userpage/usermain";
	}
	
	//顯示留言的ajax方法
	@GetMapping(value="/user/showAllCommentAjax" ,produces= "application/json")
	@ResponseBody
	public List<MessageBox> showAllComments(Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(returnNamePath());
//		List<MessageBox> userMsn = msnServiceImpl.findByUserAccount(user);
		List<MessageBox> userMsn = user.getMsnBox();
		Hibernate.initialize(userMsn);

		List<MessageBox> userMsnNull = new ArrayList<MessageBox>();
		for(int i =0; i<userMsn.size(); i++) {
			if(userMsn.get(i).getMessageBox()==null) {
				userMsnNull.add(userMsn.get(i));
				
			}	
		}
		System.out.println("放進去了");
		return userMsnNull;
	}
	
	//自己頁面新增留言的方法ajax
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
			status="訊息已傳送";
		}else {
			status="訊息傳送失敗";
		}
		return status;
	}
	
	//新增回覆留言的Ajax方法
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
		
		//找出要回覆留言的源頭
		MessageBox userMsn = msnServiceImpl.findById(id);
		
		//設定回覆留言的主人
		UserAccount userAccount = uSysServiceImpl.findByAccountIndex(userMsn.getUserAccount().getAccountIndex());
		msn.setUserAccount(userAccount);

		//設定留言者
		msn.setNetizenAccount(uSysServiceImpl.findByAccountIndex(returnNamePath()));
		
		msn.setMessageBox(userMsn);
//		userMsn.getReplyMessageBoxes().add(msn);
		Integer success =msnServiceImpl.save(msn);
		
		if(success==1) {
			message="訊息新增成功";
		}else {
			message="訊息新增失敗";

		}

		return message;
	}
	
	
	//刪除留言的Ajax方法
	@GetMapping(value="user/deleteCommentAjax" )
	@ResponseBody
	public String deleteMainComment(@RequestParam(value = "id") Integer id) {
		String message = "";
		MessageBox msn = msnServiceImpl.findById(id);
		msn.setNetizenAccount(null);
		msn.setUserAccount(null);
		msn.setReplyMessageBoxes(null);
		
		
		List<MessageBox> listMsn = msn.getReplyMessageBoxes();
		if(listMsn!=null) {
			for(MessageBox otherMSN :listMsn ) {
				otherMSN.setMessageBox(null);
				msnServiceImpl.save(otherMSN);
			}
		}

		
		Integer success =msnServiceImpl.delete(msn);
		if(success==1) {
			message="訊息刪除成功";
		}else {
			message="訊息尚未刪除";

		}

		return message;
	}
	
	//Update 更新主要留言的Ajax方法
	@PostMapping(value="/user/updateCommentAjax/", consumes= {"application/json"})
	@ResponseBody
	public String updateMainComment( @RequestBody MessageBox updateMSN) {
		String message = "";
		MessageBox msn = msnServiceImpl.findById(updateMSN.getUserMessageId());
		System.out.println("======================================="+updateMSN.getText());
		msn.setText(updateMSN.getText());
		msn.setTime(new Date());
        

		msn.setLikeAmount(updateMSN.getLikeAmount());

		Integer success =msnServiceImpl.save(msn);
		if(success==1) {
			message="訊息修改成功";
		}else {
			message="訊息尚未修改";

		}

		return message;
	}
	
	//留言按讚的方法
	@GetMapping(value="/user/updateLikeCommentAjax" )
	@ResponseBody
	public String updateLikeComment( @RequestParam(value = "id") Integer msnID) {
		String message = null;
		
		MessageBox msn = msnServiceImpl.findById(msnID);
		System.out.println("=======================================要被讚的留言在者李~~~~~~~~~~~~~~~~~~~~~~"+msn.getText());
		msn.setLikeAmount(msn.getLikeAmount()+1);
		Integer success =msnServiceImpl.save(msn);
		if(success==1) {
			message=  String.valueOf(msn.getLikeAmount());
		}else {
			message=String.valueOf(msn.getLikeAmount());

		}

		return message;
	}
	
	//留言收回按讚的方法
	@GetMapping(value="/user/updateDisLikeCommentAjax" )
	@ResponseBody
	public String updateDisLikeComment( @RequestParam(value = "id") Integer msnID) {
		String message = null;
		
		MessageBox msn = msnServiceImpl.findById(msnID);
		System.out.println("=======================================要被讚的留言在者李~~~~~~~~~~~~~~~~~~~~~~"+msn.getText());
		msn.setLikeAmount(msn.getLikeAmount()-1);
		Integer success =msnServiceImpl.save(msn);
		if(success==1) {
			message=  String.valueOf(msn.getLikeAmount());
		}else {
			message=String.valueOf(msn.getLikeAmount());

		}

		return message;
	}
	
//======================================================================================================================
	
	
	@GetMapping(path="finduser")
	@ResponseBody
	public UserAccount finduser(@RequestParam("userAccountIndex") String mainAccountIndex,Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(mainAccountIndex);	
		//Hibernate.initialize(user);
		return user;
	}
	//從Authentication取得登入者的名字字串的方法
	@GetMapping("/userLoggin/getName")
	@ResponseBody
	 public String returnNamePath(Model model) {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	  String currentUserName="";
	  if (!(authentication instanceof AnonymousAuthenticationToken)) {
	      currentUserName = authentication.getName();
	      System.out.println(currentUserName);
			UserAccount user = uSysServiceImpl.findByAccountIndex(currentUserName);		
			model.addAttribute("userAccount", user);
	      return currentUserName;
	   }
	  System.out.println("no logging user currently!!");
	   return null;
	 }
	
	//取得單個user
	@GetMapping("/usercheck/{userAccountIndex}")
	@ResponseBody
	public UserAccount findUserd(@PathVariable("userAccountIndex") String mainAccountIndex){
		UserAccount user = uSysServiceImpl.findByAccountIndex(mainAccountIndex);	
		return user;

	}
	
	
	
	//得到mainUser的friendList
	@GetMapping(path="findMainfriend/{userAccountIndex}")
	@ResponseBody
	public List<UserAccount> findMainUserFriend(@PathVariable("userAccountIndex") String mainAccountIndex){
		UserAccount user = uSysServiceImpl.findByAccountIndex(mainAccountIndex);
		 Hibernate.initialize(user.getFriends());
		Set<FriendList> mainUserFriends = user.getFriends();

		List<UserAccount> myFriend = new ArrayList<UserAccount>();
		if (mainUserFriends.isEmpty()==false) {
			for(FriendList flist:mainUserFriends) {
				UserAccount myFriendList = flist.getFriends();
				System.out.println(myFriendList.getAccountIndex());
				myFriend.add(myFriendList);
				}
			return myFriend;
		}else {
			return null;
		}
	}
	
	//好友要求(主帳號在sessionAttribute,userAccount,with session,要加的在路徑裡),簡單來說向viewUser就是送出好友要求
	@GetMapping(path="addfriend/{userAccountIndex}/{viewAccountIndex}")
	@ResponseBody
	public String requestFriend(@PathVariable("userAccountIndex") String mainAccountIndex,@PathVariable("viewAccountIndex") String friendAccountIndex,Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(mainAccountIndex);
		UserAccount friend = uSysServiceImpl.findByAccountIndex(friendAccountIndex);
		FriendList flist=new FriendList();
		flist.setMainUser(user);
		flist.setFriends(friend);
		flist.setFriendship("W");	
		friendSysServiceImpl.save(flist);

		return "request is sending";
	}
	
	//用postman判斷是否為好友關係,動態影響button的值(主帳號session,被加的是friendIndex),目前用法為在viewUser頁面時,可以動態改成符合型態
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
	
	
	//用postMap查詢到user後,將user加為好友(主身分userIndex為被加的)===看不懂,簡單來說就是登入者按下確認加入好友
	@GetMapping(path="MainUserAddFriendwithIndex/{mainUserAccountIndex}/{userAccountIndex}")
	@ResponseBody
	public String checkFriend(@PathVariable("mainUserAccountIndex") String mainAccIndx,@PathVariable("userAccountIndex") String uAccIndx,Model model) {
		//先假裝model已經登入好了
		UserAccount user =uSysServiceImpl.findByAccountIndex(mainAccIndx);		
		UserAccount friend = uSysServiceImpl.findByAccountIndex(uAccIndx);		
		//新增反向關係
		FriendList flist=new FriendList();
		flist.setMainUser(user);
		flist.setFriends(friend);
		flist.setFriendship("Y");		
		friendSysServiceImpl.save(flist);
		
		//把waitCheck變成Yes
		List<FriendList> flistOld = friendSysServiceImpl.findByFriendship(friend, user, "W");
		FriendList flist0 = flistOld.get(0);
		System.out.println("=========getListLength======="+flistOld);
		flist0.setFriendship("Y");
		friendSysServiceImpl.save(flist0);
		
		return "add finish";
	}
	
	//找到main user需要確認的好友關係(還沒用到)
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
	
	//main登入後找到誰要加main,簡單來說就是看誰對目前登入的user發出請求
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
	

	
	
	//假裝用session登入(簡單利用pathVariable模擬登入)
	@GetMapping(path="user/login/{userAccountIndex}")
	public String goViewWithSession(@PathVariable("userAccountIndex") String uAccIndx,Model model) {
		UserAccount user = uSysServiceImpl.findByAccountIndex(uAccIndx);		
		model.addAttribute("userAccount", user);
		System.out.println(returnNamePath());
		return "userpage/usermain";
	}
	
	//真的登入
	@GetMapping(path="/user/login")
	public String goUserMainSession(Model model) {
		return "userpage/usermain";
	}
	
	//用網址找到對應的user,並把user和detail存到model,傳到被看的page
	@GetMapping(path="/userView/{userAccountIndex}")
	public String goUserView(@PathVariable("userAccountIndex") String uAccIndx,Model model ) {
		UserAccount userAcc = uSysServiceImpl.findByAccountIndex(uAccIndx);
		if(userAcc!=null) {
			System.out.println(returnNamePath());

			UserDetail userAccDetail = userAcc.getUserAccountDetail();
			model.addAttribute("viewUserAccount",userAcc);
			model.addAttribute("viewUserAccountDetail", userAccDetail);
			return "userpage/viewuser";
		}
		return "userpage/viewuser";
	}
	
	//用Nickname找user(關鍵字搜尋功能)
	@GetMapping(path="searchUserByNick",produces="application/json")
	@ResponseBody
	public List<UserAccount> searchUserNickName(@RequestParam(name="name") String name,Model model){

//		UserAccount currentUser = (UserAccount)model.getAttribute("userAccount");
		UserAccount currentUser = uSysServiceImpl.findByAccountIndex(returnNamePath());
		System.out.println("currentUser======="+currentUser.getAccountIndex());
		//試試看用index判斷
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
	
	
	//從detail id傳回json
	@GetMapping(path = "/userDetail",produces="application/json")
	@ResponseBody
	public UserDetail getUserDetailById(@RequestParam(name="id") Integer detailId) {
		return uDetailService.get(detailId);
	}
	
	//從userIndex傳回整個user物件,格式為json
	@GetMapping(path="/user/{userAccountIndex}")
	@ResponseBody
	public UserAccount getUserByIndex(@PathVariable("userAccountIndex") String accIndex) {
		return uSysServiceImpl.findByAccountIndex(accIndex);
	}
	
	
	//用id找回database user照片
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
	
}
