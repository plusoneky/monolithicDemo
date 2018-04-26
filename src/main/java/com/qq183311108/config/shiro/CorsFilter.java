package com.qq183311108.config.shiro;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

//@Configuration
public class CorsFilter extends OncePerRequestFilter {

	/* 
	 * 为了支持CORS跨域
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
		response.setHeader("Access-Control-Allow-Headers","origin,x-requested-with,content-type,Accept,Access-Control-Allow-Origin,EX-SysAuthToken,EX-JSESSIONID,Authorization");
		response.setHeader("Access-Control-Max-Age", "3628800");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		
		if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
			response.setStatus(HttpStatus.OK.value());
			return ;
		}else{
			 filterChain.doFilter(request, response);
		}
		
	}	
	
}
