package com.qq1833111108.sys.service;

import com.qq1833111108.common.result.JsonResult;
import com.qq1833111108.sys.entity.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * user 用户表 服务类
 * </p>
 *
 * @author qq183311108
 * @since 2017-09-23
 */
public interface IUserService extends IService<User> {

    /**
     * 添加用户
     * @param user
     * @return
     */
    boolean addUser(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    boolean updateUser(User user);

    /**
     * 删除用户表并且删除用户角色关联表数据
     * @param id
     * @return
     */
    public boolean deleteUserByIdAndRole(String id);

}
