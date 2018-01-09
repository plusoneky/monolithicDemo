package com.qq1833111108.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qq1833111108.sys.entity.Menu;
import com.qq1833111108.sys.entity.Permission;
import com.qq1833111108.sys.entity.RolePermission;
import com.qq1833111108.sys.entity.ZNodes;
import com.qq1833111108.sys.mapper.PermissionMapper;
import com.qq1833111108.sys.mapper.RolePermissionMapper;
import com.qq1833111108.sys.service.IPermissionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.qq1833111108.sys.service.IRolePermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * permission 权限表 服务实现类
 * </p>
 *
 * @author qq183311108
 * @since 2017-09-23
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private IRolePermissionService iRolePermissionService;

    @Override
    public List<Menu> createMenu(Long uid) {
        // 创建菜单对象
        List<Menu> menus = new ArrayList<>();

        // 查询父菜单
        List<Permission> parentMenu = baseMapper.findParentMenu(uid);
        parentMenu.forEach(p -> {
            Menu menu = new Menu();
            BeanUtils.copyProperties(p, menu);
            menus.add(menu);
        });

        // 查询子菜单
        menus.forEach(m -> {
            List<Menu> temp = new ArrayList<>();
            List<Permission> children = baseMapper.findSubMenu(uid, m.getId());
            children.forEach(c -> {
                Menu menu = new Menu();
                BeanUtils.copyProperties(c, menu);
                temp.add(menu);
            });
            m.setChildren(temp);
        });
        return menus;
    }

    @Override
    public List<ZNodes> findPermissionZTreeNodes(Long roleId) {
        // 查询所有权限列表
        EntityWrapper<Permission> permissionEntityWrapper = new EntityWrapper<>();
        permissionEntityWrapper.orderBy("sort", true);
        List<Permission> permissions = baseMapper.selectList(permissionEntityWrapper);

        // 查询角色拥有那些权限
        EntityWrapper<RolePermission> rolePermissionEntityWrapper = new EntityWrapper<>();
        rolePermissionEntityWrapper.eq("rid", roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(rolePermissionEntityWrapper);

        List<ZNodes> zNodes = new ArrayList<>();
        permissions.forEach(p -> {
            ZNodes node = new ZNodes();
            BeanUtils.copyProperties(p, node);
            // 设置展开节点
            if (p.getType() == 0 || p.getType() == 1) {
                node.setOpen(true);
            }
            // 设置角色拥有的权限选中
            rolePermissions.forEach(rp -> {
                if (p.getId() == rp.getPid()) {
                    node.setChecked(true);
                }
            });
            zNodes.add(node);
        });
        return zNodes;
    }

    @Transactional
    @Override
    public boolean modifyRolePermission(List<RolePermission> rolePermissions) {
        // 删除原表角色权限
        EntityWrapper<RolePermission> rolePermissionEntityWrapper = new EntityWrapper<>();
        rolePermissionEntityWrapper.eq("rid", rolePermissions.get(0).getRid());
        rolePermissionMapper.delete(rolePermissionEntityWrapper);

        // 新增角色权限 并返回处理结果
        return iRolePermissionService.insertBatch(rolePermissions);
    }

    @Transactional
    @Override
    public boolean deletePermissionRole(String id) {
        // 删除关联表数据
        EntityWrapper<RolePermission> rolePermissionEntityWrapper = new EntityWrapper<>();
        rolePermissionEntityWrapper.eq("pid", id);
        rolePermissionMapper.delete(rolePermissionEntityWrapper);

        // 删除权限表数据
        Integer result = baseMapper.deleteById(Long.valueOf(id));

        return result >= 1 ? true : false;
    }
    
    @Override
    public List<Permission> findParentMenu(){
    	return baseMapper.findParentMenu(2L);
    }
    
    @Override
    public Page<Map<String,Object>> selectMyPage(Page<Map<String,Object>> page){
    	page.setRecords(baseMapper.selectMyPage(page));
    	return page;
    }    

}
