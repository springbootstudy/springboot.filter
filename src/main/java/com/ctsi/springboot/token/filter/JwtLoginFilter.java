package com.ctsi.springboot.token.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ctsi.springboot.token.util.JwtUtil;

@Component
@WebFilter( urlPatterns = {"/*"}, filterName = "jwtLoginFilter") 
public class JwtLoginFilter implements Filter  {
	
	private static final Logger log = Logger.getLogger(JwtLoginFilter.class);
	
    private List<String> excludedPageList;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("## init ");

		excludedPageList = new ArrayList<String>();
		Enumeration<String> enums = filterConfig.getInitParameterNames();
		String key;
		String value;
		while ( enums.hasMoreElements() ) {
			key = enums.nextElement();
			value = filterConfig.getInitParameter(key);
			
			log.info("## " + key + ", " + value);
			
			excludedPageList.add(value);
		}
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		log.info("## doFilter ");
		
		HttpServletRequest req = (HttpServletRequest) request;
		log.info("## " + req.getServletPath());
		
		boolean isExcludedPage = false;
		for (String page : excludedPageList) {

			// 判断当前URL是否与例外页面相同
			if (req.getServletPath().equals(page)) { 
				log.info("## " + page + " , you're excluded.");
				isExcludedPage = true;
				break;
			}
			
		}
		
		if (isExcludedPage) { // 在过滤url之外
			chain.doFilter(request, response);
		} 
		else { // 不在过滤url之外
			response.setContentType("application/json; charset=utf-8");
			
			String token = req.getHeader("token");
			log.info("## " + token);
			// 通过验证 true
			if (StringUtils.isEmpty(token)) {
				log.info("## token为空");
				
				try ( Writer writer = response.getWriter() ) {
					writer.write("请登录系统");
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// 否则 false
			else {
				try {
//					JwtUtil.validateToken(token);
					Claims claims = JwtUtil.getClaimsFromToken(token);
					Date date = claims.getExpiration();
					long tokenTime = date.getTime();
					log.info("## 获取Token的时间 " + tokenTime + ", " + new Date(tokenTime));
					long curTime = System.currentTimeMillis();
					log.info("## 当前时间 " + curTime + ", " + new Date(curTime));
					
					if (curTime > tokenTime) {
						try ( Writer writer = response.getWriter() ) {
							writer.write("token 过期，请重新获取");
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					else {
						log.info("## 通过验证");
						chain.doFilter(request, response);
					}
				}
				catch (ExpiredJwtException ex) {
					log.info("## token 过期");
					ex.printStackTrace();
					try ( Writer writer = response.getWriter() ) {
						writer.write("token 过期，请重新获取");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				catch (Exception ex) {
					ex.printStackTrace();
					log.info("## 解析token出错");
					
					try ( Writer writer = response.getWriter() ) {
						writer.write("token 不正确");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
	}

	@Override
	public void destroy() {
		log.info("## destroy ");
		log.info("## destroy ");
		
	}
	

}