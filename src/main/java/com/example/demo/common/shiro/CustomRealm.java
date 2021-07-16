package com.example.demo.common.shiro;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.dao.authentication.RolePermissionMapper;
import com.example.demo.dao.authentication.UserMapper;
import com.example.demo.dao.authentication.UserRoleMapper;
import com.example.demo.domain.authc.RolePermission;
import com.example.demo.domain.authc.User;
import com.example.demo.domain.authc.UserRole;
import com.example.demo.domain.base.BaseModel;
import com.example.demo.utils.cryptology.Md5Encrypt;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义 认证、授权
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    @Qualifier("userMapper")
    private UserMapper userMapper;
    @Resource
    @Qualifier("userRoleMapper")
    private UserRoleMapper userRoleMapper;
    @Resource
    @Qualifier("rolePermissionMapper")
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 身份认证
     * credentialsSalt = username + salt
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        String pwd = new String(token.getPassword());
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(pwd)) {
            throw new AccountException("用户名或密码不正确");
        }

        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userName)
                .eq(BaseModel::getDeleteFlag, 0));
        if (user == null) {
            throw new UnknownAccountException("用户名或密码不正确");
        }

        String password = user.getPassword();
        String salt = user.getSalt();
        String passwordTest = Md5Encrypt.getMD5Mac(pwd + salt);
        if (!password.equals(passwordTest)) {
            throw new AccountException("用户名或密码不正确");
        }

        return new SimpleAuthenticationInfo(userName, password, ByteSource.Util.bytes(salt), getName());
    }

    /**
     * 权限认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        User user = (User) principalCollection.getPrimaryPrincipal();
        String userName = user.getUserName();
        if (user == null || StringUtils.isBlank(userName)) {
            throw new UnknownAccountException("未知账户");
        }

        user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userName)
                .eq(BaseModel::getDeleteFlag, 0));
        if (user == null) {
            throw new UnknownAccountException("未知账户");
        }

        List<UserRole> userRoles = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId, user.getId())
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
