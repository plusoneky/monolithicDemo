package com.qq183311108.config.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.qq183311108.common.utils.StringUtil;
import com.qq183311108.sys.entity.Permission;
import com.qq183311108.sys.entity.User;
import com.qq183311108.sys.mapper.PermissionMapper;
import com.qq183311108.sys.service.IUserService;

/**
 * Author: qq183311108
 * Email: 183311108@qq.com
 * Date: 2017/9/1
 * Time: 00:00
 * Describe: 自定义ShiroRealm 认证 授权
 */
public class MyShiroRealm extends AuthorizingRealm {

    private static final transient Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    private IUserService iUserService;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 认证信息.(身份验证)
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户的输入的账号.
        String username = (String)token.getPrincipal();

        //查询用户信息
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("username", username);
        User user = iUserService.selectOne(wrapper);
        if (user == null) {
            return null;
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),"MyShiroRealm");
        
        return simpleAuthenticationInfo;
    }

    /**
     * 权限信息.(授权):
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取用户信息 
    	//这里转换类型如果有错，请去掉maven中依赖的spring-boot-devtools进行尝试，或者配置spring-devtools.properties文件中restart.include.shiro
        User user = (User) principals.getPrimaryPrincipal();   

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 查询用户拥有那些权限
        List<Permission> permissions = permissionMapper.findUserPermission(user.getId());
        List<String> list = new ArrayList<String>();
        // 添加权限代码
        for (Permission item: permissions) {
            if (StringUtil.isNotEmpty(item.getPermCode()))
                list.add(item.getPermCode());
        }
        authorizationInfo.addStringPermissions(list);

        return authorizationInfo;
    }

}
