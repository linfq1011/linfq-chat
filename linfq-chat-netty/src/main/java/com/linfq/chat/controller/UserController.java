package com.linfq.chat.controller;

import com.linfq.chat.common.util.ResultVo;
import com.linfq.chat.model.User;
import com.linfq.chat.service.UserService;
import com.linfq.chat.vo.UserVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * UserController.
 *
 * @author linfq
 * @date 2019/7/21 19:32
 */
@RequestMapping("/u")
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/registOrLogin")
	public ResultVo hello(@RequestBody User user) {

		// 0. 判断用户名和密码不能为空
		if(StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
			return ResultVo.errorMsg("用户名或密码不能为空...");
		}

		// 1. 判断用户名是否存在，如果存在就登录，如果不存在则注册
		boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

		User userResult = null;
		if (usernameIsExist) {
			// 1.1 登录
			Optional<User> userOptional = userService.queryUserForLogin(user.getUsername(), DigestUtils.md5Hex(user.getPassword()));
			if (!userOptional.isPresent()) {
				return ResultVo.errorMsg("用户名或密码不正确...");
			}
			userResult = userOptional.get();
		} else {
			// 1.2 注册
			user.setNickname(user.getUsername());
			user.setFaceImage("");
			user.setFaceImageBig("");
			user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			userResult = this.userService.saveUser(user);
		}

		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(userResult, userVo);

		return ResultVo.ok(userVo);
	}
}
