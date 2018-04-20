package com.ctsi.springboot.token.security;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ctsi.springboot.token.filter.JwtAuthenticationFilter;
import com.ctsi.springboot.token.filter.JwtLoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter  {
	
	private static final Logger log = Logger.getLogger(SecurityConfiguration.class); 

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("## configure HttpSecurity");
		
		http.cors().and().csrf().disable()
			.authorizeRequests()
			.antMatchers("/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new JwtLoginFilter(authenticationManager()))
			.addFilter(new JwtAuthenticationFilter(authenticationManager()));
		
//		super.configure(http);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		log.info("## configure AuthenticationManagerBuilder" );
		auth.inMemoryAuthentication()
			.passwordEncoder(new BCryptPasswordEncoder());
		
		super.configure(auth);
	}

}
