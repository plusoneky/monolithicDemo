package com.qq1833111108.config.shiro;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qq1833111108.biz.controller.FirmwareVersionController;

public class MyAuthenticationFilter extends FormAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(MyAuthenticationFilter.class);

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		String loginUrl = getLoginUrl();
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		try {
			logger.info("没有权限：" + httpServletRequest.getRequestURL());
			httpServletRequest.getRequestDispatcher(loginUrl).forward(httpServletRequest, httpServletResponse);
		} catch (ServletException e) {
			logger.error("", e);
		}

	}

	@Override
	protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
        response.setHeader("Access-Control-Allow-Headers", "origin,x-requested-with,content-type,Accept,Access-Control-Allow-Origin,EX-SysAuthToken,EX-JSESSIONID,Authorization");
        response.setHeader("Access-Control-Max-Age", "3628800");
        response.setHeader("Access-Control-Allow-Credentials","true");		
        
		System.out.println("request.getRequestURI()="+request.getRequestURI());
		System.out.println("request.getMethod()="+request.getMethod());
		
		if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
			response.setStatus(HttpStatus.OK.value());
			return false;
		}

		
		return super.preHandle(request, response);
	}
}
