package com.infotran.springboot.commonmodel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infotran.springboot.loginsystem.service.Impl.UserAccountServiceImpl;

@Service
public class AuthUserDetailsService implements UserDetailsService {

	@Autowired
	private UserAccountServiceImpl userAccountService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = userAccountService.getByName(username);
		
		return new AuthUserDetails(userAccount);
	}
	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		UserAccount uProfiles = userAccountService.getByName(username);
//		return new User(uProfiles.getAccountIndex(), uProfiles.getPassword(), Collections.emptyList());
//	}
	
//    private List<SimpleGrantedAuthority> convertToSimpleAuthorities(List<UserAuthority> authorities) {
//        return authorities.stream()
//                .map(auth -> new SimpleGrantedAuthority(auth.name()))
//                .collect(Collectors.toList());
//    }

}
