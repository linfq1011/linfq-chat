package com.linfq.chat.controller;

import com.linfq.chat.common.util.FastDFSClient;
import com.linfq.chat.common.util.FileUtils;
import com.linfq.chat.common.util.ResultVo;
import com.linfq.chat.model.User;
import com.linfq.chat.service.UserService;
import com.linfq.chat.vo.UserBo;
import com.linfq.chat.vo.UserVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	@Autowired
	private FastDFSClient fastDFSClient;

	/**
	 * 用户登录/注册.
	 *
	 * @param user
	 * @return
	 */
	@PostMapping("/registOrLogin")
	public ResultVo registOrLogin(@RequestBody User user) {

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

	/**
	 * 上传用户头像.
	 *
	 * @param userBo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/uploadFaceBase64")
	public ResultVo uploadFaceBase64(@RequestBody UserBo userBo) throws Exception {
		// 获取前端传过来的base64字符串，然后转换为文件对象再上传
		String base64Data = userBo.getFaceData();
		String userFacePath = "D:\\" + userBo.getUserId() + "userface64.png";
		FileUtils.base64ToFile(userFacePath, base64Data);

		// 上传文件到fastdfs
		MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
		String url = fastDFSClient.uploadBase64(faceFile);
		System.out.println(url);

		// 获取缩略图的url
		String thump = "_80x80.";
		String[] arr = url.split("\\.");
		String thumpImgUrl = arr[0] + thump + arr[1];

		// 更新用户头像
		User user = new User();
		user.setId(userBo.getUserId());
		user.setFaceImage(thumpImgUrl);
		user.setFaceImageBig(url);
		this.userService.update(user);

		return ResultVo.ok(user);
	}

}
