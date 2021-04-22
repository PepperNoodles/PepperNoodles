package com.infotran.springboot.websocket.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.infotran.springboot.websocket.storage.UserStorage;



@RestController
@CrossOrigin
public class UserController {

	
	
	@GetMapping("/registration/{userName}")
	public ResponseEntity<Void> register(@PathVariable String userName){
		System.out.println("handling register User request:"+userName);		
			try {
				UserStorage.getInstance().setUser(userName);
				System.out.println("ok");
				System.out.println("size= "+UserStorage.getInstance().getUsers().size());
			} catch (Exception e) {
				System.out.println("handling register User failed");
				return ResponseEntity.badRequest().build();			
			}			
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/fetchAllUser")
	public Set<String> fetchAll(){
		return UserStorage.getInstance().getUsers();
	}
}
