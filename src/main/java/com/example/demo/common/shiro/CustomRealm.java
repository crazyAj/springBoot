package com.example.demo.common.shiro;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.dao.authentication.RolePermissionMapper;
import com.example.demo.dao.authentication.UserMapper;
import com.example.demo.dao.authentication.UserRoleMapper;
import com.example.demo.domain.authentication.RolePermission;
import com.example.demo.domain.authentication.User;
import com.example.demo.domain.authentication.UserRole;
import com.example.demo.domain.base.BaseModel;
import com.example.demo.utils.cryptology.Md5Encrypt;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.provider.MD5;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义 认证、授权
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 身份认证
     * credentialsSalt = username + salt
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        if (userName == null) throw new AccountException("用户名或密码不正确");

        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userName)
                .eq(BaseModel::getDeleteFlag, 0));
        if (users == null || users.size() < 0) throw new UnknownAccountException("用户名或密码不正确");

        User user = users.get(0);
        String password = user.getPassword();
        String salt = user.getSalt();
        String pwd = new String((char[]) authenticationToken.getCredentials());
        String passwordTest = Md5Encrypt.getMD5Mac(pwd + salt);
        if (!password.equals(passwordTest)) throw new AccountException("用户名或密码不正确");

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, password,
                ByteSource.Util.bytes(salt), getName());
        return simpleAuthenticationInfo;
    }

    /**
     * 权限认证
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        String userName = (String) principalCollection.getPrimaryPrincipal();

        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userName)
                .eq(BaseModel::getDeleteFlag, 0));
        if (users == null || users.size() < 0) throw new UnknownAccountException("未知账户");

        List<UserRole> userRoles = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId, users.get(0).getUnid())
                .eq(BaseModel::getDeleteFlag, 0));
        if (userRoles != null && userRoles.size() > 0) {
            List<String> roleIds = userRoles.stream().map(UserRole::getRoleId).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
            simpleAuthorizationInfo.addRoles(roleIds);

            List<RolePermission> rolePermissions = rolePermissionMapper.selectList(Wrappers.<RolePermission>lambdaQuery()
                    .in(RolePermission::getRoleId, roleIds)
                    .eq(BaseModel::getDeleteFlag, 0));
            if (rolePermissions != null && rolePermissions.size() > 0) {
                List<String> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
                simpleAuthorizationInfo.addStringPermissions(permissionIds);
            } else {
                throw new AuthorizationException("未知权限");
            }
        } else {
            throw new AuthorizationException("未知角色");
        }
        return simpleAuthorizationInfo;
    }

}
