package com.qq183311108.sys.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qq183311108.common.controller.BaseController;
import com.qq183311108.common.result.JsonResult;
import com.qq183311108.common.utils.StringUtil;
import com.qq183311108.sys.entity.Role;
import com.qq183311108.sys.entity.User;
import com.qq183311108.sys.entity.UserRole;
import com.qq183311108.sys.service.IRoleService;
import com.qq183311108.sys.service.IUserRoleService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * role 角色表 前端控制器
 * </p>
 *
 * @author qq183311108
 * @since 2017-09-23
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {
    
    private IRoleService iRoleService;
    
    private IUserRoleService iUserRoleService;
    
    @Autowired
    RoleController(IRoleService iRoleService, IUserRoleService iUserRoleService){
    	this.iRoleService = iRoleService;
    	this.iUserRoleService = iUserRoleService;
    }

    /**
     * 角色列表页面
     * @return
     */
    @GetMapping("/list")
    public String list() {
        return "/admin/role/list";
    }

    /**
     * 获取角色列表
     * @param pageNumber 当前页
     * @param pageSize 每页显示条数
     * @param searchText 搜索名称
     * @return
     */
    @ResponseBody
    @PostMapping("/getList")
    public Map<String, Object> getUserList(int pageNumber, int pageSize, String searchText) {
        Map<String,Object> result = new HashMap<String,Object>();
        Page<Role> page = new Page<>(pageNumber, pageSize);
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        if (StringUtil.isNotEmpty(searchText)) {
            wrapper.like("name", searchText);
        }
        wrapper.orderBy("sort", true);
        Page<Role> rolePage = iRoleService.selectPage(page, wrapper);
        result.put("total", rolePage.getTotal());
        result.put("rows", rolePage.getRecords());
        return result;
    }

    /**
     * 添加用户
     * @param role
     * @return
     */
    @ResponseBody
    @PostMapping("/addRole")
    public JsonResult addRole(Role role) {
        return iRoleService.insert(role) ? renderSuccess("添加成功") : renderError("添加失败");
    }

    /**
     * 修改用户
     * @param role
     * @return
     */
    @ResponseBody
    @PostMapping("/updateRole")
    public JsonResult updateRole(Role role) {
        return iRoleService.updateById(role) ? renderSuccess("修改成功") : renderError("修改失败");
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(@RequestParam(value = "id", required = false) String id) {
        if (StringUtil.isEmpty(id)) {
            return renderError("请选择数据");
        }
        return iRoleService.deleteRoleByIdAndPermission(id) ? renderSuccess("删除成功") : renderError("删除失败");
    }

    /**
     * 分配角色
     * 查询用户拥有的角色
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUserRole")
    public JsonResult getUserRole(@RequestBody User user) {
        EntityWrapper<UserRole> wrapper = new EntityWrapper<>();
        wrapper.eq("uid", user.getId());
        List<UserRole> userRoles = iUserRoleService.selectList(wrapper);
        return renderSuccess(userRoles);
    }

    /**
     * 分配用户角色
     * @return
     */
    @RequiresPermissions("sys.user.role:add")
    @ResponseBody
    @RequestMapping("/modifyUserRole")
    public JsonResult modifyUserRole(@RequestBody List<UserRole> userRoles) {
        return iRoleService.modifyUserRole(userRoles) ? renderSuccess("分配成功"): renderError("分配失败");
    }

}
