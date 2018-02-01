package com.qq183311108.sys.service;

import com.qq183311108.sys.entity.Role;
import com.baomidou.mybatisplus.service.IService;
import com.qq183311108.sys.entity.UserRole;

import java.util.List;

/**
 * <p>
 * role 角色表 服务类
 * </p>
 *
 * @author qq183311108
 * @since 2017-09-23
 */
public interface IRoleService extends IService<Role> {

    /**
     * 删除角色表并且删除角色权限表关联数据
     * @param id
     * @return
     */
    boolean deleteRoleByIdAndPermission(String id);

    /**
     * 分配用户角色
     * @param userRoles
     * @return
     */
    boolean modifyUserRole(List<UserRole> userRoles);
}
