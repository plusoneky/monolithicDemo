package com.qq183311108.sys.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.qq183311108.AbstractTestController;

public class PermissionControllerTest extends AbstractTestController{

	@Test
	public void testList() throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.get("/admin/permission/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
                .session(getLoginSession())) 
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("后台权限管理")));	        
        
	}
	
	@Test
	public void testGetPermissionList() throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.get("/admin/permission/getList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
				.session(getLoginSession())) 
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
                .andReturn();
	}

}
