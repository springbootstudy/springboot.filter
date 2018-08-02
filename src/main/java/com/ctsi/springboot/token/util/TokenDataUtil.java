package com.ctsi.springboot.token.util;

import io.jsonwebtoken.Claims;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ctsi.springboot.token.entity.TokenData;

/**
 * 
 * @author lb
 *
 * @since 2018年8月1日
 *
 */
public class TokenDataUtil {
	
	private static final Logger log = Logger.getLogger(TokenDataUtil.class);
	
//	public static String get(HttpServletRequest req, String key) {
//		Claims claims = (Claims) req.getAttribute("tokenData");
//		return (String) claims.get(key);
//	}
	
	public static Optional<TokenData> getData(HttpServletRequest req) {
		Claims claims = (Claims) req.getAttribute("tokenData");
		
		Optional<TokenData> opt;
		if (null == claims) {
			opt = Optional.empty();
		}
		
		try {
			String json = JacksonUtil.bean2Json(claims.get(Constants.TOKEN_DATA));
			log.info("## " + json);
			
			TokenData data = JacksonUtil.json2Bean(json, TokenData.class);
			
			opt = Optional.ofNullable(data);
		} 
		catch (Exception e) {
			e.printStackTrace();
			opt = Optional.empty();
		}
		
		return opt;
	}

}
