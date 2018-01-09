package com.qq1833111108.sys.service;

import com.qq1833111108.sys.entity.Menu;
import com.qq1833111108.sys.entity.Permission;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.qq1833111108.sys.entity.RolePermission;
import com.qq1833111108.sys.entity.ZNodes;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * permission 权限表 服务类
 * </p>
 *
 * @author qq183311108
 * @since 2017-09-23
 */
public interface IPermissionService extends IService<Permission> {

    /**
     * 创建登陆用户菜单
     * @param uid
     * @return
     */
    List<Menu> createMenu(Long uid);

    /**
     * 查询所有权限树形展示 并且选中角色拥有的树节点
     * @param roleId 角色ID
     * @return
     */
    List<ZNodes> findPermissionZTreeNodes(Long roleId);

    /**
     * 修改角色权限
     * @param rolePermissions
     * @return
     */
    boolean modifyRolePermission(List<RolePermission> rolePermissions);

    /**
     * 删除权限 和 权限角色表数据
     * @param id
     * @return
     */
    boolean deletePermissionRole(String id);
    
    List<Permission> findParentMenu();
    
    Page<Map<String,Object>> selectMyPage(Page<Map<String,Object>> page);
}
