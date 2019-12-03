package org.kettle.scheduler.system.biz.service;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.PageHelper;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.request.UserReq;
import org.kettle.scheduler.system.api.response.UserRes;
import org.kettle.scheduler.system.biz.constant.Constant;
import org.kettle.scheduler.system.biz.entity.User;
import org.kettle.scheduler.system.biz.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户管理业务逻辑层
 *
 * @author lyf
 */
@Service
public class SysUserService {
    private final UserRepository userRepository;

    public SysUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(UserReq req) {
        User user = BeanUtil.copyProperties(req, User.class);
		user.setPassword(new Sha256Hash(user.getPassword(),Constant.salt,Constant.hashIterations).toString());
        userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
		Optional<User> optional = userRepository.findById(id);
		if (!optional.isPresent()) {
			throw new MyMessageException("删除对象不存在");
		}
		User user = optional.get();
		if ("admin".equals(user.getAccount())) {
			throw new MyMessageException("无法删除管理员账户");
		}
		userRepository.delete(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        List<User> users = userRepository.findAllById(ids);
		List<User> collect = users.stream().filter(user -> !"admin".equals(user.getAccount())).collect(Collectors.toList());
		userRepository.deleteInBatch(collect);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(UserReq req) {
        Optional<User> optional = userRepository.findById(req.getId());
        if (optional.isPresent()) {
            User user = optional.get();
            BeanUtil.copyProperties(req, user);
            // 不能修改账户名称
			user.setAccount(null);
            userRepository.save(user);
        }
    }

    public PageOut<UserRes> findUserListByPage(UserReq query, PageHelper page) {
        // 排序
        Sort sort = page.getSorts().isEmpty() ? Sort.by(Sort.Direction.DESC, "addTime") : page.getSorts();
        // 查询
        User user = BeanUtil.copyProperties(query, User.class);
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase();
        Example<User> example = Example.of(user, matcher);
        Page<User> pageList = userRepository.findAll(example, PageRequest.of(page.getNumber(), page.getSize(), sort));
        // 封装数据
        List<UserRes> collect = pageList.get().map(t -> BeanUtil.copyProperties(t, UserRes.class)).collect(Collectors.toList());
        return new PageOut<>(collect, pageList.getNumber(), pageList.getSize(), pageList.getTotalElements());
    }

    public UserRes getUserDetail(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.map(user -> BeanUtil.copyProperties(user, UserRes.class)).orElse(null);
    }

    public User getUserByAccount(String account) {
    	return userRepository.findByAccount(account);
	}
}
