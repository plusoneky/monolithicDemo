package com.qq183311108.sys.mapper;

import com.qq183311108.sys.entity.Permission;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * permission 权限表 Mapper 接口
 * </p>
 *
 * @author qq183311108
 * @since 2017-09-23
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 查询父菜单
     * @param uid 用户ID
     * @return
     */
    List<Permission> findParentMenu(Long uid);

    /**
     * 查询子菜单
     * @param uid 用户ID
     * @param pid 父ID
     * @return
     */
    List<Permission> findSubMenu(@Param("uid")Long uid, @Param("pid")Long pid);

    /**
     * 查询用户拥有那些权限
     * @param uid
     * @return
     */
    List<Permission> findUserPermission(Long uid);
    
    
    
    List<Map<String,Object>> selectMyPage(Pagination page);

}