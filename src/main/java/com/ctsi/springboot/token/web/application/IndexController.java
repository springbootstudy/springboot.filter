package com.ctsi.springboot.token.web.application;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ctsi.springboot.token.config.FilterConfig;
import com.ctsi.springboot.token.util.JwtUtil;

@RestController
public class IndexController {
	
	private static final Logger logger = Logger.getLogger(IndexController.class);
	
	@Autowired
	private FilterConfig filterConfig;
	
	@RequestMapping("/index")
	public String index() {
		logger.info("## Index");
		
		return "OK";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object login(@RequestBody User user) {
		String username = user.getUsername();
		String passwd = user.getPasswd();
		
		logger.info("## post login " + username + ", " + passwd);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 通过认证的账号
		if ("a".equals(username) && "b".equals(passwd)) {
			logger.info("## token flag " + filterConfig.isFilterToken());
			if (filterConfig.isFilterToken()) {
				Map<String, Object> claims = new HashMap<>();
				String token = JwtUtil.generateToken(claims);
				logger.info("## " + token);
				
				map.put("token", token);
			}
		}
		// 不通过
		else {
			map.put("error", HttpStatus.UNAUTHORIZED);
		}
		
		return map;
	}
	
	@RequestMapping("/login")
	public Object login(String username, String passwd) {
		logger.info("## get login " + username + ", " + passwd);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 通过认证的账号
		if ("a".equals(username) && "b".equals(passwd)) {
			logger.info("## token flag " + filterConfig.isFilterToken());
			if (filterConfig.isFilterToken()) {
				Map<String, Object> claims = new HashMap<>();
				String token = JwtUtil.generateToken(claims);
				logger.info("## " + token);
				
				map.put("token", token);
			}
		}
		// 不通过
		else {
			map.put("error", HttpStatus.UNAUTHORIZED);
		}
		
		return map;
	}

}

class User {
	
	private String username;
	private String passwd;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
}
