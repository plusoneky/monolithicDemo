package com.qq1833111108.config.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.qq1833111108.common.dto.ApiResDto;
import com.qq1833111108.common.exception.MyException;
import com.qq1833111108.common.exception.MyException.CommErrCode;
import com.qq1833111108.frimware.controller.FrimwareProtocolController;
import com.qq1833111108.sys.entity.User;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MyKickoutSessionControlFilter extends AccessControlFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(MyKickoutSessionControlFilter.class);

    private String kickoutUrl; //踢出后到的地址
    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;
    
    private static final String SHIRO_KICKOUT_SESSION_KEY_NAME = "shiro-kickout-session";

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(SHIRO_KICKOUT_SESSION_KEY_NAME);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        User user = (User) subject.getPrincipal();
        String username = user.getUsername();
        
        Serializable sessionId = session.getId();

        //TODO 同步控制，读取缓存   没有就存入
        Deque<Serializable> deque = cache.get(username);
        if(deque == null) {
        	//将sessionId存入队列
            deque = new LinkedList<Serializable>();
            //将用户的sessionId队列缓存
            cache.put(username, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
            cache.put(username, deque);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception
            	//logger.error("",e);
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if ((Boolean)session.getAttribute("kickout")!=null&&(Boolean)session.getAttribute("kickout") == true) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) { //ignore
            	logger.error("",e);
            }
            saveRequest(request);
            
            
			//判断是不是Ajax请求
			if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
				
				ApiResDto resDto = new ApiResDto(new MyException(CommErrCode.KickOutByAnotherLogin));
				//输出json串
				ajaxOut(response, resDto);
			}else{
				//重定向
				WebUtils.issueRedirect(request, response, kickoutUrl);
			}            
            return false;
        }
        return true;
    }
    
    private void ajaxOut(ServletResponse hresponse, ApiResDto resDto)
			throws IOException {
		try {
			hresponse.setCharacterEncoding("UTF-8");
			PrintWriter out = hresponse.getWriter();
			out.println(JSON.toJSONString(resDto));
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("KickoutSessionFilter.class 输出JSON异常，可以忽略。",e);
		}
	}    
    
	@Override
	protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		System.out.println("-----------------Origin-------------------------"+request.getHeader("Origin"));
		
		if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
	        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
	        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
	        response.setHeader("Access-Control-Allow-Headers", "origin,x-requested-with,content-type,Accept,Access-Control-Allow-Origin,EX-SysAuthToken,EX-JSESSIONID,Authorization");
	        response.setHeader("Access-Control-Max-Age", "3628800");
	        response.setHeader("Access-Control-Allow-Credentials","true");		
	        
			System.out.println("request.getRequestURI()="+request.getRequestURI());
			System.out.println("request.getMethod()="+request.getMethod());			
			
			response.setStatus(HttpStatus.OK.value());
			return false;
		}

		return super.preHandle(request, response);
	}    
}
