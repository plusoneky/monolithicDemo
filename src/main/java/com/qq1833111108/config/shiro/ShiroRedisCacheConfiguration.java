package com.qq1833111108.config.shiro;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
/**
 * Author: qq183311108
 * Email: 183311108@qq.com
 * Date: 2017/9/1
 * Time: 00:00
 * Describe: Shiro 单机配置，生产环境如果没有集成redis，可以使用本配置，实现单机会话管理，使用的是ehCache缓存
 */
@Configuration
public class ShiroRedisCacheConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(ShiroRedisCacheConfiguration.class);

    /**
     *  Shiro 过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        logger.info("ShiroConfiguration.shirFilter()");
        
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();

        // SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 登陆页面
        shiroFilterFactoryBean.setLoginUrl("/admin/login.html");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/admin/index");
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/500.html");
        

        
        Map<String, Filter> filters =  shiroFilterFactoryBean.getFilters();
        filters.put("kickout", myKickoutSessionControlFilter());        
        
        // 拦截器.
        Map<String,String> filterChainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();//   new LinkedHashMap<String,String>();
        
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/admin/logout", "logout");

        // 过滤链
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/plugins/**", "anon");
        filterChainDefinitionMap.put("/500.html", "perms");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/admin/mylogin", "anon");
        filterChainDefinitionMap.put("/admin/register.html", "anon"); // 注册界面
        filterChainDefinitionMap.put("/admin/register", "anon"); // 注册提交数据
        filterChainDefinitionMap.put("/admin/sencCode", "anon"); // 发送邮箱验证码
        filterChainDefinitionMap.put("/admin/isUsername/**", "anon"); // 判断用户名是否存在
        filterChainDefinitionMap.put("/admin/isEmail/**", "anon"); // 判断邮箱是否存在

        //swagger
		filterChainDefinitionMap.put("/*/api-docs", "anon");
		filterChainDefinitionMap.put("/swagger*", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/configuration/*", "anon");
		filterChainDefinitionMap.put("/*/configuration/*", "anon");
		filterChainDefinitionMap.put("/callback*", "anon");
		
		//终端业务
		filterChainDefinitionMap.put("/tp/**", "anon");

        filterChainDefinitionMap.put("/**", "kickout,authc");    //注意，这里要加上kickout
        /**
         * anon:所有url都都可以匿名访问;
         * authc: 需要认证才能进行访问;
         * user:配置记住我或认证通过可以访问；
         */
        return shiroFilterFactoryBean;
    }
    
    @Bean
    public SecurityManager securityManager(){
    	DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    	securityManager.setRealm(myShiroRealm());
    	securityManager.setSessionManager(sessionManager());
    	securityManager.setCacheManager(redisCacheManager());
    	return securityManager;
    }    
    
    @Bean
    public MyShiroRealm myShiroRealm(){
    	MyShiroRealm myShiroRealm = new MyShiroRealm();
    	myShiroRealm.setCredentialsMatcher(credentialsMatcher());
		return myShiroRealm;
    }      
       
    @Bean
    public RedisManager redisManager(){
    	RedisManager redisManager = new RedisManager();
    	redisManager.setHost("192.168.1.200");
    	redisManager.setPort(6379);
    	redisManager.setPassword("123456");
    	redisManager.setExpire(604800000);
    	return redisManager;   	
    }
    
    @Bean
    public RedisCacheManager redisCacheManager(){
    	RedisCacheManager redisCacheManager = new RedisCacheManager();
    	redisCacheManager.setRedisManager(redisManager());
    	return redisCacheManager;
    }   
    
    @Bean
    public SessionDAO redisSessionDAO(){
    	RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
    	redisSessionDAO.setRedisManager(redisManager());
    	redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
    	return redisSessionDAO;
    } 
    
    @Bean
    public SessionManager sessionManager(){
    	DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    	sessionManager.setSessionDAO(redisSessionDAO());
    	sessionManager.setGlobalSessionTimeout(604800000);
    	sessionManager.setSessionIdCookie(sessionIdCookie());
    	sessionManager.setSessionValidationSchedulerEnabled(true);
    	return sessionManager;
    }     
    
    @Bean
    public Cookie sessionIdCookie(){
    	SimpleCookie sessionIdCookie = new SimpleCookie();
    	sessionIdCookie.setDomain(".testcors.com");
    	sessionIdCookie.setName("accessFirmToken");
		return sessionIdCookie;
    }    
    
    @Bean
    public SessionIdGenerator sessionIdGenerator(){
    	return new MySessionIdGenerator();
    }

    @Bean
    public CredentialsMatcher credentialsMatcher(){
    	HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
    	hashedCredentialsMatcher.setHashAlgorithmName("md5");
    	hashedCredentialsMatcher.setHashIterations(1);
    	return hashedCredentialsMatcher;
    }
    
    @Bean
    public MyKickoutSessionControlFilter myKickoutSessionControlFilter(){
    	MyKickoutSessionControlFilter myKickoutSessionControlFilter = new MyKickoutSessionControlFilter();
    	//使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
    	//这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
    	//也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性    	
    	myKickoutSessionControlFilter.setCacheManager(redisCacheManager());
    	//用于根据会话ID，获取会话进行踢出操作的；
    	myKickoutSessionControlFilter.setSessionManager(sessionManager());
    	//是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
    	myKickoutSessionControlFilter.setKickoutAfter(false);
    	//同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
    	myKickoutSessionControlFilter.setMaxSession(1);
    	//被踢出后重定向到的地址；
    	myKickoutSessionControlFilter.setKickoutUrl("/admin/login.html");
    	return myKickoutSessionControlFilter;
    }

    /**
     * Shiro生命周期处理器 * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全
     *  逻辑验证 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能 * @return
     * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * shiro注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
