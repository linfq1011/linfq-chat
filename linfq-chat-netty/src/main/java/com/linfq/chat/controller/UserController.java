package com.linfq.chat.controller;

import com.linfq.chat.common.constant.OperatorFriendRequestTypeEnum;
import com.linfq.chat.common.constant.SearchFriendsStatusEnum;
import com.linfq.chat.common.util.FastDFSClient;
import com.linfq.chat.common.util.FileUtils;
import com.linfq.chat.common.util.ResultVo;
import com.linfq.chat.model.User;
import com.linfq.chat.service.FriendRequestService;
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
	@Autowired
	private FriendRequestService friendRequestService;

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
		user = this.userService.save(user);

		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);

		return ResultVo.ok(userVo);
	}

	/**
	 * 修改昵称.
	 *
	 * @param userBo
	 * @return
	 */
	@PostMapping("/setNickname")
	public ResultVo setNickname(@RequestBody UserBo userBo) {
		// TODO 参数校验

		// 更新用户头像
		User user = new User();
		user.setId(userBo.getUserId());
		user.setNickname(userBo.getNickname());
		this.userService.save(user);

		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);

		return ResultVo.ok(userVo);
	}

	/**
	 * 搜索好友接口, 根据账号做匹配查询而不是模糊查询.
	 *
	 * @param myUserId 当前用户id
	 * @param friendUsername 朋友用户名
	 * @return
	 */
	@PostMapping("/search")
	public ResultVo searchUser(Integer myUserId, String friendUsername) {
		// 0. 判断 myUserId friendUsername 不能为空
		if (myUserId == null || StringUtils.isBlank(friendUsername)) {
			return ResultVo.errorMsg("");
		}

		// 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
		// 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
		// 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
		SearchFriendsStatusEnum status = userService.preconditionSearchFriends(myUserId, friendUsername);
		if (SearchFriendsStatusEnum.SUCCESS == status) {
			Optional<User> userOptional = this.userService.getByUsername(friendUsername);
			User user = userOptional.get();
			UserVo userVo = new UserVo();
			BeanUtils.copyProperties(user, userVo);
			return ResultVo.ok(userVo);
		} else {
			return ResultVo.errorMsg(status.msg);
		}
	}

	/**
	 * 发送添加好友的请求.
	 *
	 * @param myUserId 当前用户id
	 * @param friendUsername 朋友用户名
	 * @return
	 */
	@PostMapping("/addFriendRequest")
	public ResultVo addFriendRequest(Integer myUserId, String friendUsername) {
		// 0. 判断 myUserId friendUsername 不能为空
		if (myUserId == null || StringUtils.isBlank(friendUsername)) {
			return ResultVo.errorMsg("");
		}

		// 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
		// 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
		// 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
		SearchFriendsStatusEnum status = userService.preconditionSearchFriends(myUserId, friendUsername);
		if (SearchFriendsStatusEnum.SUCCESS == status) {
			userService.sendFriendRequest(myUserId, friendUsername);
		} else {
			return ResultVo.errorMsg(status.msg);
		}

		return ResultVo.ok();
	}

	/**
	 * 查询好友请求
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping("/queryFriendRequests")
	public ResultVo queryFriendRequests(Integer userId) {
		// 0. 判断 myUserId friendUsername 不能为空
		if (userId == null) {
			return ResultVo.errorMsg("");
		}
		// 1. 查询用户接收到的朋友申请
		return ResultVo.ok(friendRequestService.listByAcceptUserId(userId));
	}

	/**
	 * 通过或者忽略朋友请求
	 *
	 * @param acceptUserId
	 * @param sendUserId
	 * @param operType
	 * @return
	 */
	@PostMapping("/operFriendRequest")
	public ResultVo queryFriendRequests(Integer acceptUserId, Integer sendUserId, Integer operType) {
		// 0. acceptUserId sendUserId operType 判断不能为空
		if (acceptUserId == null || sendUserId == null || operType == null) {
			return ResultVo.errorMsg("");
		}

		// 1. 如果operType 没有对应的枚举值，则直接抛出空错误信息
		if (StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
			return ResultVo.errorMsg("");
		}

		if (OperatorFriendRequestTypeEnum.IGNORE.type.equals(operType)) {
			// 2. 判断如果忽略好友请求，则直接删除好友请求的数据库表记录
			friendRequestService.delete(sendUserId, acceptUserId);
		} else if (OperatorFriendRequestTypeEnum.PASS.type.equals(operType)) {
			// 3. 判断如果是通过好友请求，则互相增加好友记录到数据库对应的表
			//	   然后删除好友请求的数据库表记录
			friendRequestService.passFriendRequest(sendUserId, acceptUserId);
		}

		// TODO 刷新通讯录

		return ResultVo.ok();
	}
}
