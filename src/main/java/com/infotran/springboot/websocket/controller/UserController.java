package com.infotran.springboot.websocket.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.userAccsystem.service.UserSysService;
import com.infotran.springboot.websocket.storage.UserStorage;


@SessionAttributes(names = "userAccount")
@RestController
@CrossOrigin
public class UserController {
	
	@Autowired
	UserSysService uSysServiceImpl;
	
	@GetMapping("/userMessageLoggin/getName")
	 public String returnNamePath(Model model) {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	  String currentUserName="";
	  if (!(authentication instanceof AnonymousAuthenticationToken)) {
	      currentUserName = authentication.getName();
	      System.out.println(currentUserName);
			UserAccount user = uSysServiceImpl.findByAccountIndex(currentUserName);	
			System.out.println(user.getAccountIndex());
			model.addAttribute("userAccount", user);
	      return currentUserName;
	   }
	  System.out.println("no logging user currently!!");
	   return null;
	 }
	
	
	@GetMapping("/registration/{userName}")
	public ResponseEntity<Void> register(@PathVariable String userName,Model model){
		System.out.println("handling register User request:"+userName);
		 UserAccount user = (UserAccount)model.getAttribute("userAccount");
		 String logInUserName = user.getAccountIndex();
		if (UserStorage.getInstance().getUsers().contains(logInUserName)) {
			return ResponseEntity.ok().build();
		}
		else {
			try {
				UserStorage.getInstance().setUser(userName);
				System.out.println("ok");
				System.out.println("size= "+UserStorage.getInstance().getUsers().size());
			} catch (Exception e) {
				System.out.println("handling register User failed");
				return ResponseEntity.badRequest().build();			
			}			
		}
			
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/fetchAllUser")
	public Set<String> fetchAll(){
		return UserStorage.getInstance().getUsers();
	}
}
