package com.qq1833111108.config.shiro;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

/**
 * Author: qq183311108
 * Email: 183311108@qq.com
 * Date: 2018/1/15
 * Time: 00:00
 * Describe: Shiro集群配置，集成了Redis，支持分布式session共享，默认使用本配置
 */
@Configuration
public class ShiroRedisConfiguration {

    private static final transient Logger log = LoggerFactory.getLogger(ShiroRedisConfiguration.class);

    /**
     * Shiro 过滤器
     * @param securityManager
     * @return
     */
    @Bean(name="shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
        log.info("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();

        // SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 登陆页面
        shiroFilterFactoryBean.setLoginUrl("/admin/login.html");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/admin/index");
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/500.html");

        Map<String, Filter> filters = new LinkedHashMap<String,Filter>();
        filters.put("authc", new MyAuthenticationFilter());
        filters.put("kickout", new MyKickoutSessionControlFilter());
        shiroFilterFactoryBean.setFilters(filters);        
        
        // 拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();

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
        
        filterChainDefinitionMap.put("/test*", "anon"); // 测试
        //filterChainDefinitionMap.put("/admin/user/getList*", "anon"); // 测试
        
        //swagger
		filterChainDefinitionMap.put("/*/api-docs", "anon");
		filterChainDefinitionMap.put("/swagger*", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/configuration/*", "anon");
		filterChainDefinitionMap.put("/*/configuration/*", "anon");
		filterChainDefinitionMap.put("/callback*", "anon");
		
		//终端业务
		filterChainDefinitionMap.put("/tp/**", "anon");
		
        filterChainDefinitionMap.put("/**", "authc");
        /**
         * anon:所有url都都可以匿名访问;
         * authc: 需要认证才能进行访问;
         * user:配置记住我或认证通过可以访问；
         */

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        
        return shiroFilterFactoryBean;
    }
        

    /**
     * Shiro 安全管理器 
     * @return
     */
    @Bean(name="securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthorizingRealm authRealm){
    	System.err.println("--------------支持分布式session共享shiro已经加载----------------");
    	DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(authRealm);
        securityManager.setCacheManager(ehCacheManager());
        return securityManager;
    }

    /**
     * 身份认证realm
     * @return
     */
    @Bean(name="authRealm")
    public AuthorizingRealm authRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("MD5"); // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1); // 散列的次数，比如散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;
    }

    /**
     * 配置Shiro 缓存
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return cacheManager;
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
