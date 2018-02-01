package com.qq183311108.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.qq183311108.common.result.JsonResult;
import com.qq183311108.sys.entity.User;
import com.qq183311108.sys.entity.UserRole;
import com.qq183311108.sys.mapper.UserMapper;
import com.qq183311108.sys.mapper.UserRoleMapper;
import com.qq183311108.sys.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * user 用户表 服务实现类
 * </p>
 *
 * @author qq183311108
 * @since 2017-09-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean addUser(User user) {
        String salt = String.valueOf(System.currentTimeMillis());
        SimpleHash password = new SimpleHash("MD5", user.getUsername(), salt);
        user.setSalt(salt);
        user.setPassword(password.toString());
        Integer result = baseMapper.insert(user);
        if (result >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        Integer result = baseMapper.updateById(user);
        if (result >= 1) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteUserByIdAndRole(String id) {
        // 拼接用户表删除list
        String[] split = id.split(",");
        List<String> userId = new ArrayList<>();
        for (String item:split) {
            userId.add(item);
        }
        // 删除用户表数据
        Integer integer = baseMapper.deleteBatchIds(userId);

        // 删除用户角色关联表数据
        EntityWrapper<UserRole> userRoleEntityWrapper = new EntityWrapper<>();
        userRoleEntityWrapper.in("uid", id);
        userRoleMapper.delete(userRoleEntityWrapper);

        return true;
    }

}
