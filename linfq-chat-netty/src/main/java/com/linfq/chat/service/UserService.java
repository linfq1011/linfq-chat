package com.linfq.chat.service;

import com.linfq.chat.common.orm.BaseService;
import com.linfq.chat.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Optional;

/**
 * UserService.
 *
 * @author linfq
 * @date 2019/7/21 17:26
 */
@Service
public class UserService extends BaseService<User> {

	/**
	 * 判断用户名是否存在.
	 *
	 * @param username
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public boolean queryUsernameIsExist(String username) {
		User user4Query = new User();
		user4Query.setUsername(username);
		return this.get(user4Query).isPresent();
	}

	/**
	 * 查询用户是否存在.
	 *
	 * @param username
	 * @param pwd
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public Optional<User> queryUserForLogin(String username, String pwd) {
		Example userExample = new Example(User.class);
		Example.Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("username", username);
		criteria.andEqualTo("password", pwd);
		return this.getByExample(userExample);
	}

	/**
	 * 注册.
	 *
	 * @param user
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public User saveUser(User user) {
		// TODO 为每个用户生成一个唯一的二维码
		user.setQrcode("");
		this.add(user);
		return user;
	}
}
