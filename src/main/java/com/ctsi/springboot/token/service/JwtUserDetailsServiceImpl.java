package com.ctsi.springboot.token.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements	UserDetailsService {

	private static final Logger log = Logger.getLogger(JwtUserDetailsServiceImpl.class);
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		log.info("== UserDetails loadUserByUsername " + username);
		
		return new User("a", "b", new ArrayList<GrantedAuthority>());
	}

}
