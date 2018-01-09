package com.qq1833111108.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qq1833111108.FirmwareServerApplication;
import com.qq1833111108.sys.entity.Permission;
import com.qq1833111108.sys.service.IPermissionService;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=FirmwareServerApplication.class)// 指定spring-boot的启动类 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PermissionServiceImplTest {

    @Autowired  
    private IPermissionService permissionServiceImpl;  
    
	@Test
	public void test101_findParentMenu() {
		List<Permission> list = permissionServiceImpl.findParentMenu();
		System.out.println("1:"+JSON.toJSON(list).toString());
	}

	@Test
	public void test102_selectList() {
        EntityWrapper<Permission> permissionEntityWrapper = new EntityWrapper<>();
        permissionEntityWrapper.eq("type", 0);
        permissionEntityWrapper.orderBy("sort", true);
        List<Permission> list = permissionServiceImpl.selectList(permissionEntityWrapper);
        System.out.println("2:"+JSON.toJSON(list).toString());
	}
	
	@Test
	public void test103_selectPage() {
        Page<Permission> page = new Page<>(1, 10);
        EntityWrapper<Permission> permissionEntityWrapper = new EntityWrapper<>();
        Page<Permission> permissionPage = permissionServiceImpl.selectPage(page, permissionEntityWrapper);
        System.out.println("3:"+JSON.toJSON(permissionPage).toString());
        
	}	
	
	@Test
	public void test104_selectMyPage() {
		Page<Map<String,Object>> page1 = new Page<>(1, 2);
        Page<Map<String,Object>> map1 = permissionServiceImpl.selectMyPage(page1);
        System.out.println("4:"+JSON.toJSON(map1).toString());
        System.out.println("5:"+JSON.toJSON(page1).toString());
        
        Page<Map<String,Object>> page2 = new Page<>(1, 10);
        permissionServiceImpl.selectMyPage(page2);
        System.out.println("6:"+JSON.toJSON(page2).toString());        
        
	}	
}
