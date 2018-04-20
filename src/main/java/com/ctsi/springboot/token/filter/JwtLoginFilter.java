package com.ctsi.springboot.token.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ctsi.springboot.token.entity.LoginUser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	private static final Logger log = Logger.getLogger(JwtLoginFilter.class);
	
	private AuthenticationManager authenticationManager;
	
	public JwtLoginFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	// 接收并解析用户凭证
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		log.info("## attemptAuthentication ");
		
		try {
			LoginUser lu = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
			log.info("## " + lu.getUsername() + ", " + lu.getPasswd());
			
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							lu.getUsername(), lu.getPasswd(), new ArrayList<>()));
		} 
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
//		return super.attemptAuthentication(request, response);
	}

	// 登录成功被调用
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("## successfulAuthentication");
		
		String token = Jwts.builder()
                .setSubject(((User) authResult.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, "nc.moc.istc")
                .compact();
		log.info("## " + token);
		
		response.addHeader("Authorization", token);
		
//		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	

}
