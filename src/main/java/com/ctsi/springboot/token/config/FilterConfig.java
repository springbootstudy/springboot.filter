package com.ctsi.springboot.token.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.ctsi.springboot.token.filter.JwtLoginFilter;

@Configuration
@Component
public class FilterConfig {
	
	private static final Logger log = Logger.getLogger(FilterConfig.class);
	
	@Value("${filter.token}")
	private boolean filterToken = true;
	
	@Autowired
	private JwtLoginFilter jwtLoginFilter;
	
	@Bean
	public FilterRegistrationBean<Filter> filterRegistrationBean() {
		log.info("### " + filterToken);
		
		FilterRegistrationBean<Filter> reg = new FilterRegistrationBean<Filter>();
		reg.setFilter(jwtLoginFilter);
//		reg.addInitParameter("login", "/login");
		
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("login", "/login");
		initParameters.put("other", "/other");
		reg.setInitParameters(initParameters);
		
		reg.setEnabled(filterToken);
		
		
		return reg;
	}

	public boolean isFilterToken() {
		return filterToken;
	}

	public void setFilterToken(boolean filterToken) {
		this.filterToken = filterToken;
	}

}
