package com.ctsi.springboot.token.web.application;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
	
	private static final Logger logger = Logger.getLogger(IndexController.class);
	
	@RequestMapping("/index")
	public String index() {
		logger.info("## Index");
		
		return "OK";
	}
	
	@RequestMapping("/login")
	public String login(String username, String passwd) {
		logger.info("## login " + username + ", " + passwd);
		
		// 通过
		if ("a".equals(username) && "b".equals(passwd)) {
			
		}
		// 不通过
		else {
			
		}
		
		return "login";
	}

}
