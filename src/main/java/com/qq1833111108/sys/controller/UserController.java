package com.qq1833111108.sys.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qq1833111108.common.controller.BaseController;
import com.qq1833111108.common.result.JsonResult;
import com.qq1833111108.common.utils.StringUtil;
import com.qq1833111108.sys.entity.Role;
import com.qq1833111108.sys.entity.User;
import com.qq1833111108.sys.service.IRoleService;
import com.qq1833111108.sys.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * user 用户表 前端控制器
 * </p>
 *
 * @author qq183311108
 * @since 2017-09-23
 */
@Api(value = "用户相关接口")
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

    private IUserService iUserService;
    
    private IRoleService iRoleService;
    
    @Autowired
    UserController(IUserService iUserService, IRoleService iRoleService){
    	this.iUserService = iUserService;
    	this.iRoleService = iRoleService;
    }

    /**
     * 用户列表页面
     * @return
     */
    @RequiresPermissions("sys.user:list")
    @GetMapping("/list")
    public String list(Model model) {
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        wrapper.or("sort", true);
        List<Role> allRole = iRoleService.selectList(wrapper);
        model.addAttribute("allRole", allRole);
        return "/admin/user/list";
    }

    /**
     * 获取用户列表
     * @param pageNumber 当前页
     * @param pageSize 每页显示条数
     * @param searchText 搜索名称
     * @return
     */
    //@CrossOrigin(origins = "*", maxAge = 3600)
    //@RequiresPermissions("sys.user:list")
    @ResponseBody
    @RequestMapping("/getList")
    public Map<String, Object> getUserList(int pageNumber, int pageSize, String searchText, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
		response.setHeader("Access-Control-Allow-Headers","origin,x-requested-with,content-type,Accept,Access-Control-Allow-Origin,EX-SysAuthToken,EX-JSESSIONID,Authorization"); //, x-requested-with,Access-Control-Allow-Origin,EX-SysAuthToken,EX-JSESSIONID
		response.setHeader("Access-Control-Max-Age", "3628800");
        response.setHeader("Access-Control-Allow-Credentials","true");	
		Map<String,Object> result = new HashMap<String,Object>();
        Page<User> page = new Page<>(pageNumber, pageSize);
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        if (StringUtil.isNotEmpty(searchText)) {
            wrapper.like("username", searchText);
        }
        Page<User> userPage = iUserService.selectPage(page, wrapper);
        result.put("total", userPage.getTotal());
        result.put("rows", userPage.getRecords());
        return result;
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequiresPermissions("sys.user:add")
    @ResponseBody
    @RequestMapping(value = "/addUser")
    public JsonResult addUser(User user) {
        // 创建盐, 散列加密
        String salt = String.valueOf(System.currentTimeMillis());
        SimpleHash password = new SimpleHash("MD5", user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(password.toString());
        return  iUserService.insert(user) ? renderSuccess("添加成功") : renderError("添加失败");
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @RequiresPermissions("sys.user:update")
    @ResponseBody
    @RequestMapping(value = "/updateUser")
    public JsonResult updateUser(User user) {
        return iUserService.updateById(user) ? renderSuccess("修改成功") : renderError("修改失败");
    }

    /**
     * 审核用户
     * @param users
     * @return
     */
    @RequiresPermissions("sys.user:optionUserStatus")
    @ResponseBody
    @RequestMapping(value = "/optionUserStatus")
    public JsonResult optionUserStatus(@RequestBody List<User> users) {
        for (User user:users) {
            user.setNickname(null);
            user.setPassword(null);
            user.setUsername(null);
            user.setEmail(null);
            user.setSalt(null);
            user.setStatus(1);
        }
        return iUserService.updateBatchById(users) ? renderSuccess("审核成功") : renderError("审核失败");
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequiresPermissions("sys.user:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(@RequestParam(value = "id", required = false) String id) {
        if (StringUtil.isEmpty(id)) {
            return renderError("请选择数据");
        }
        return iUserService.deleteUserByIdAndRole(id) ? renderSuccess("删除成功") : renderError("删除失败");
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @ApiOperation(value = "修改密码", notes = "新增系统功能", response = JsonResult.class, httpMethod = "POST")
    @ResponseBody
    @RequestMapping(value = "/updateUserPwd")
    public JsonResult updateUserPwd(@ApiParam(required = true, value = "旧密码") @RequestParam(value = "oldPwd", required = true) String oldPwd,@ApiParam(required = true, value = "新密码") @RequestParam(value = "newPwd", required = true) String newPwd) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(getCurrentLoginUsername(), oldPwd);
		try {
			subject.login(token);
		} catch (IncorrectCredentialsException e) {
			return renderError("旧密码不正确");
			
		}  
      // 创建盐, 散列加密
      String salt = String.valueOf(System.currentTimeMillis());
      SimpleHash password = new SimpleHash("MD5", newPwd, salt);
      User user = new User();
      user.setId(getCurrentLoginId());
      user.setSalt(salt);
      user.setPassword(password.toString());
      
      return iUserService.updateById(user) ? renderSuccess("修改密码成功") : renderError("修改密码失败");		
		
    }         
}
