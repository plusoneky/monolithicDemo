package com.qq183311108.sys.controller;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.qq183311108.AbstractTestController;

public class UserControllerTest extends AbstractTestController {

	/**
	 * 测试认证（用例：使用正确的的账号、密码，成功登录。预期登录成功后进入主页）
	 * @throws Exception
	 */
	@Test
	public void test101_Authentication() throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.get("/admin/mylogin")
			    .contentType(MediaType.APPLICATION_JSON_UTF8)
			    .accept(MediaType.APPLICATION_JSON)
			    .param("username", ADMIN_USERNAME)
			    .param("password", ADMIN_PWD))
			    .andExpect(MockMvcResultMatchers.status().isOk())
			    .andExpect(MockMvcResultMatchers.forwardedUrl("/admin/index.html"))
			    .andReturn();
	}     
    
	/**
	 * 测试认证（用例：使用一个系统不存在的账号登录。预期重定向到登录页面）
	 * @throws Exception
	 */
	@Test
	public void test102_Authentication() throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.post("/admin/mylogin")
		        .contentType(MediaType.APPLICATION_JSON_UTF8)
		        .param("username", "notexist")
		        .param("password", "123456")
		        .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().isOk())
			    .andExpect(MockMvcResultMatchers.view().name("/admin/login"))
		        .andDo(MockMvcResultHandlers.print());
	}   	
	
	/**
	 * 测试认证（用例：使用存在的账号，但密码输入错误登录。预期重定向到登录页面）
	 * @throws Exception
	 */
	@Test
	public void test103_Authentication() throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.post("/admin/mylogin")
		        .contentType(MediaType.APPLICATION_JSON_UTF8)
		        .param("username", ADMIN_USERNAME)
		        .param("password", "wrong123")
		        .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.view().name("/admin/login"))
		        .andDo(MockMvcResultHandlers.print());
	} 	
	
	/**
	 * 测试鉴权（用例：用户登录认证后，访问已授权的URL。预期成功返回该页面）
	 * @throws Exception
	 */
	@Test
	public void test201_AuthorizationInfo() throws Exception {
		MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/admin/user/list")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON)
            .session(getLoginSession(ADMIN_USERNAME,ADMIN_PWD))) 
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/admin/user/list"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("allRole"))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("用户管理")))
			.andDo(MockMvcResultHandlers.print())
			.andReturn();
		Assert.assertNotNull(result.getModelAndView().getModel().get("allRole"));
	} 
	
	/**
	 * 测试鉴权（用例：使用登录错误的账号密码的会话，访问已为该用户授权的URL。预期跳转到登录页）
	 * @throws Exception
	 */
	@Test
	public void test202_AuthorizationInfo() throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.get("/admin/user/list")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON)
            .session(getLoginSession("notexist","123456"))) 
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/login.html"))
			.andDo(MockMvcResultHandlers.print())
			.andReturn();
	}	
	
	/**
	 * 测试鉴权（用例：使用没有登录的会话，访问需要授权的URL。预期跳转到登录页）
	 * @throws Exception
	 */
	@Test
	public void test203_AuthorizationInfo() throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.get("/admin/user/list")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/login.html"))
			.andDo(MockMvcResultHandlers.print())
			.andReturn();
	}	

	/**
	 * 测试鉴权（用例：使用一个正确的账号密码登录，去访问未对该用户授权的URL。预期跳转到登录页）
	 * @throws Exception
	 */
	@Test
	public void test204_AuthorizationInfo() throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.get("/admin/user/list")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON)
            .session(getLoginSession(NORMAL_USERNAME,NORMAL_PWD))) 
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("admin/403"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("exception"))
			.andDo(MockMvcResultHandlers.print())
			.andReturn();
	} 	


}
