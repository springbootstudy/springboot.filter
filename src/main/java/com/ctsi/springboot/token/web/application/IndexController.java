package com.ctsi.springboot.token.web.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ctsi.springboot.token.config.FilterConfig;
import com.ctsi.springboot.token.entity.TokenData;
import com.ctsi.springboot.token.util.Constants;
import com.ctsi.springboot.token.util.JwtUtil;
import com.ctsi.springboot.token.util.TokenDataUtil;

@RestController
public class IndexController {
	
	private static final Logger log = Logger.getLogger(IndexController.class);
	
	@Autowired
	private FilterConfig filterConfig;
	
	@RequestMapping("/index")
	public ResponseEntity<String> index(HttpServletRequest req, HttpSession session) {
		log.info("## Index " + session.getId());
		
//		Claims claims = (Claims) req.getAttribute("tokenData");
//		String username = (String) claims.get(Constants.USER_USERNAME);
//		Integer userid = (Integer) claims.get(Constants.USER_USERID);
//		log.info("## " + username + ", " + userid);
		
		Optional<TokenData> data = TokenDataUtil.getData(req);
		data.ifPresent(new Consumer<TokenData>() {

			@Override
			public void accept(TokenData t) {
				log.info("## " + t.getUserid() + ", " + t.getUsername());
			}
			
		});
		
//		Claims claims = (Claims) req.getAttribute("tokenData");
//		try {
//			log.info("## " + JacksonUtil.bean2Json(claims));
//		} 
//		catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return new ResponseEntity<String>("OK-" + session.getId(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object login(@RequestBody User user) {
		String username = user.getUsername();
		String passwd = user.getPasswd();
		
		log.info("## post login " + username + ", " + passwd);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 通过认证的账号
		if ("a".equals(username) && "b".equals(passwd)) {
			log.info("## token flag " + filterConfig.isFilterToken());
			if (filterConfig.isFilterToken()) {
				Map<String, Object> claims = new HashMap<>();
				String token = JwtUtil.generateToken(claims);
//				logger.info("## " + token);
				
				map.put("token", token);
			}
		}
		// 不通过
		else {
			map.put("error", HttpStatus.UNAUTHORIZED);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/logine", method = RequestMethod.POST)
	public ResponseEntity<Object> logine(@RequestBody User user) {
		String username = user.getUsername();
		String passwd = user.getPasswd();
		
		log.info("## post login " + username + ", " + passwd);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 通过认证的账号
		if ("a".equals(username) && "b".equals(passwd)) {
			log.info("## token flag " + filterConfig.isFilterToken());
			if (filterConfig.isFilterToken()) {
				TokenData tokenData = new TokenData();
				tokenData.setUserid(2);
				tokenData.setUsername(username);
				
				Map<String, Object> claims = new HashMap<>();
				claims.put(Constants.TOKEN_DATA, tokenData);
//				claims.put(Constants.USER_USERNAME, username);
//				claims.put(Constants.USER_USERID, 2);
				String token = JwtUtil.generateToken(claims);
//				logger.info("## " + token);
				
				map.put("token", token);
			}
		}
		// 不通过
		else {
			map.put("error", HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	
	@RequestMapping("/login")
	public Object login(String username, String passwd) {
		log.info("## get login " + username + ", " + passwd);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 通过认证的账号
		if ("a".equals(username) && "b".equals(passwd)) {
			log.info("## token flag " + filterConfig.isFilterToken());
			if (filterConfig.isFilterToken()) {
				Map<String, Object> claims = new HashMap<>();
				String token = JwtUtil.generateToken(claims);
//				logger.info("## " + token);
				
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
