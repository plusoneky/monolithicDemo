package com.qq183311108.core.interceptor;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.qq183311108.core.Constants;
import com.qq183311108.core.util.WebUtil;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;

/**
 * 国际化信息设置(基于SESSION)
 * 
 */
@Component
public class LocaleInterceptor extends BaseInterceptor {
	protected static final Logger logger = LoggerFactory.getLogger(LocaleInterceptor.class);
	static UASparser uasParser;

	static {
		try {
			uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		// 设置客户端语言
		Locale locale = (Locale) session.getAttribute("LOCALE");
		if (locale == null) {
			String language = request.getParameter("locale");
			if (StringUtils.isNotBlank(language)) {
				locale = new Locale(language);
				session.setAttribute("LOCALE", locale);
			} else {
				locale = request.getLocale();
			}
		}
		LocaleContextHolder.setLocale(locale);
		// 客户端IP
		String clientIp = (String) session.getAttribute(Constants.USER_IP);
		if (clientIp == null) {
			session.setAttribute(Constants.USER_IP, WebUtil.getHost(request));
		}
		// 客户端代理
		String userAgent = (String) session.getAttribute(Constants.USER_AGENT);
		if (userAgent == null) {
			try {
				UserAgentInfo userAgentInfo = uasParser.parse(request.getHeader("user-agent"));
				userAgent = userAgentInfo.getOsName() + " " + userAgentInfo.getType() + " " + userAgentInfo.getUaName();
				String uuid = request.getHeader("UUID");
				if ("unknown unknown unknown".equals(userAgent) && StringUtils.isNotBlank(uuid)) {
					userAgent = uuid;
				}
				session.setAttribute(Constants.USER_AGENT, userAgent);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return super.preHandle(request, response, handler);
	}
}
