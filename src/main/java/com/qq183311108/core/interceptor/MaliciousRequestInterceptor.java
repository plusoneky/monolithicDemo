package com.qq183311108.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qq183311108.common.exception.HttpCode;
import com.qq183311108.config.properties.SysProperties;
import com.qq183311108.core.Constants;

/**
 * 恶意请求拦截器
 * 
 */
@Component
public class MaliciousRequestInterceptor extends BaseInterceptor {
	private boolean allRequest = false;
	
	//@Value("${sysProperties.minRequestIntervalTime}")
	private long minRequestIntervalTime;
	
	//@Value("${sysProperties.maxMaliciousTimes}")
	private long maxMaliciousTimes;
	
	@Autowired
	MaliciousRequestInterceptor(SysProperties sysProperties){
		this.minRequestIntervalTime = sysProperties.getMinRequestIntervalTime();
		this.maxMaliciousTimes = sysProperties.getMaxMaliciousTimes();
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getServletPath();
		if (url.endsWith("/unauthorized") || url.endsWith("/forbidden")) {
			return super.preHandle(request, response, handler);
		}

		//终端的请求不受此制约
//		if (url.contains("/tp/get_new_ver") || url.contains("/tp/save_update_log")) {
//			return super.preHandle(request, response, handler);
//		}		
		
		HttpSession session = request.getSession();
		String preRequest = (String) session.getAttribute(Constants.PREREQUEST);
		Long preRequestTime = (Long) session.getAttribute(Constants.PREREQUEST_TIME);
		if (preRequestTime != null && preRequest != null) { // 过滤频繁操作
			if ((url.equals(preRequest) || allRequest)
					&& System.currentTimeMillis() - preRequestTime < minRequestIntervalTime) {
				Long maliciousRequestTimes = (Long) session.getAttribute(Constants.MALICIOUS_REQUEST_TIMES);
				if (maliciousRequestTimes == null) {
					maliciousRequestTimes = 1L;
				} else {
					maliciousRequestTimes++;
				}
				session.setAttribute(Constants.MALICIOUS_REQUEST_TIMES, maliciousRequestTimes);
				if (maliciousRequestTimes > maxMaliciousTimes) {
					response.setStatus(HttpCode.MULTI_STATUS.value());
					logger.error("MaliciousRequestInterceptor: To intercept a malicious request : {}", url);
					return false;
				}
			} else {
				session.setAttribute(Constants.MALICIOUS_REQUEST_TIMES, 0L);
			}
		}
		session.setAttribute(Constants.PREREQUEST, url);
		session.setAttribute(Constants.PREREQUEST_TIME, System.currentTimeMillis());
		return super.preHandle(request, response, handler);
	}

}
