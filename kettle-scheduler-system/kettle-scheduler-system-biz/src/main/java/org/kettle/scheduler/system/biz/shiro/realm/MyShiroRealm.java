package org.kettle.scheduler.system.biz.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.system.biz.constant.Constant;
import org.kettle.scheduler.system.biz.entity.User;
import org.kettle.scheduler.system.biz.service.SysUserService;

import javax.annotation.Resource;

/**
 * shiro数据操作相关类
 * 因为继承关系无法@Component注入
 * 需要手动@bean方式注入
 *
 * @author lyf
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource(type = SysUserService.class)
    private SysUserService userService;

	/**
	 * 告诉shiro如何根据获取到的用户信息中的密码和盐值来校验
	 */
	private void setEncryptInfo() {
        HashedCredentialsMatcher hashMatcher = new HashedCredentialsMatcher();
        // 加密算法的名称
        hashMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        // 配置加密的次数
        hashMatcher.setHashIterations(Constant.hashIterations);
        // 是否存储为16进制
        hashMatcher.setStoredCredentialsHexEncoded(true);
        this.setCredentialsMatcher(hashMatcher);
    }

    /**
     * 授权过程, 从数据库中查询该用户具有的角色和功能
     *
     * @param principalCollection 用户信息体, 由认证过程中{@code SimpleAuthenticationInfo(user, user.getPassword(), "ShiroRealm")}的第一个入参传入过来
     * @return {@link AuthorizationInfo}
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 从{doGetAuthenticationInfo}传过来的信息中获取用户对象
		User user = (User) principalCollection.getPrimaryPrincipal();
        // 把角色和权限添加到授权器
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if ("admin".equals(user.getAccount())) {
			authorizationInfo.addRole("admin");
		} else {
			authorizationInfo.addRole("user");
		}
        return authorizationInfo;
    }

    /**
     * 认证过程
     *
     * @param authenticationToken 主体信息, 由subject.login({@link AuthenticationToken});传入
     * @return {@link AuthenticationInfo}
     * @throws AuthenticationException 认证异常信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 从主体传过来的信息中获取用户名
        String userName = String.valueOf(authenticationToken.getPrincipal());
        // 通过用户名去数据库中获取密码凭证
        User user = userService.getUserByAccount(userName);
        if (user == null) {
            throw new UnknownAccountException();
        }
        // 把查询的信息放入验证对象中, getName()是获取当前Realm对象的名称
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        if (StringUtil.hasText(Constant.salt)) {
			setEncryptInfo();
            // 如果存在盐, 就添加盐的验证
            info.setCredentialsSalt(ByteSource.Util.bytes(Constant.salt));
        }
        return info;
    }

}
