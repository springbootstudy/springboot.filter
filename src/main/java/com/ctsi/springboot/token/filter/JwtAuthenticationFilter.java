package com.ctsi.springboot.token.filter;

import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
	
	private static final Logger log = Logger.getLogger(JwtAuthenticationFilter.class);

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("$$ doFilterInternal");
		
		String header = request.getHeader("Authorization");
		log.info("$$ " + header);

		if (header == null || !header.startsWith("")) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authenticationToken = getUsernamePasswordAuthenticationToken(header);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
		
//		super.doFilterInternal(request, response, chain);
	}
	
	private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
	      String user = Jwts.parser()
	              .setSigningKey("nc.moc.istc")
	              .parseClaimsJws(token)
	              .getBody()
	              .getSubject();

	      if (null != user) {
	          return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
	      }

	      return null;
	  }

}
