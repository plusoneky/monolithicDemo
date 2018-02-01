package com.qq183311108;

import javax.servlet.Filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ActiveProfiles("dev")
public class AbstractTestController {
	
	//系统管理员账号
	public static final String ADMIN_USERNAME = "admin"; 
	
	//系统管理员密码
	public static final String ADMIN_PWD = "admin123"; 
	
	//普通账号
	public static final String NORMAL_USERNAME = "lijiabei"; 
	
	//普通账号密码
	public static final String NORMAL_PWD = "admin"; 
	
	private MockMvc mockMvc;
	
	private MockHttpSession loginSession;  
    
    @Autowired  
    private WebApplicationContext webApplicationContext;  	
	
	public MockMvc getMockMvc() {
		return mockMvc;
	}

	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	public MockHttpSession getLoginSession() {
		return loginSession;
	}

	@Before
	public void setUp() throws Exception {
		Filter filter = (Filter) webApplicationContext.getBean("shiroFilter");
		DefaultWebSecurityManager securityManager = webApplicationContext.getBean(DefaultWebSecurityManager.class);
		
		//单个类  拦截器无效
		//mockMvc = MockMvcBuilders.standaloneSetup(new  IndexController(),new PermissionController()).addFilters(filter).build();//设置要mock的Controller类，可以是多个
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(filter).build();  
		SecurityUtils.setSecurityManager(securityManager);
		ThreadContext.bind(securityManager);
		
		//默认填充一个登录的会话
		loginSession = getLoginSession(ADMIN_USERNAME,ADMIN_PWD);
	}	
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void login(String username,String password) throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/mylogin")
	        .contentType(MediaType.APPLICATION_JSON_UTF8)
	        .param("username", username)
	        .param("password", password)
	        .accept(MediaType.APPLICATION_JSON))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	        .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/index.html"))
	        .andDo(MockMvcResultHandlers.print());
    }

	/** 
	 * 退出
	 * @return 
	 * @throws Exception 
	 */  	
	public void logout() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/logout")
	        .contentType(MediaType.APPLICATION_JSON_UTF8)
	        .accept(MediaType.APPLICATION_JSON))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	        .andExpect(MockMvcResultMatchers.redirectedUrl("admin/login.html"))
		    .andDo(MockMvcResultHandlers.print());
	}	
	
    /** 
     * 获取登入信息session 
     * @return 
     * @throws Exception 
     */  
    public MockHttpSession getLoginSession(String username,String password){  
    	MockHttpSession returnValue = new MockHttpSession();
    	MvcResult result;
		try {
			result = mockMvc  
			    .perform(MockMvcRequestBuilders.get("/admin/mylogin")
			    .contentType(MediaType.APPLICATION_JSON_UTF8)
			    .accept(MediaType.APPLICATION_JSON)
			    .param("username", username)
			    .param("password", password))
			    .andReturn();
			returnValue = (MockHttpSession)result.getRequest().getSession();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return returnValue;  
    } 
    	
	@Test
	public void testList() throws Exception {
		MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/admin/user/list")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON)
            .session(getLoginSession())) 
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/admin/user/list"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("allRole"))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("用户管理")))
			//.andDo(MockMvcResultHandlers.print())
			.andReturn();
		Assert.assertNotNull(result.getModelAndView().getModel().get("allRole"));
	}
	
	

}
