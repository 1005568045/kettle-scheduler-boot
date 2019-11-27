package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.User;

/**
 * @author lyf
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据登录账户名查询用户信息
     * @param account 账号
     * @return {@link User}
     */
    User findByAccount(String account);
}