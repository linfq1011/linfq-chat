package com.linfq.chat.service;

import com.linfq.chat.common.constant.SearchFriendsStatusEnum;
import com.linfq.chat.common.orm.BaseService;
import com.linfq.chat.common.util.FastDFSClient;
import com.linfq.chat.common.util.FileUtils;
import com.linfq.chat.common.util.QRCodeUtils;
import com.linfq.chat.model.FriendRequest;
import com.linfq.chat.model.MyFriend;
import com.linfq.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Optional;

/**
 * UserService.
 *
 * @author linfq
 * @date 2019/7/21 17:26
 */
@Service
public class UserService extends BaseService<User> {

	@Autowired
	private QRCodeUtils qrCodeUtils;
	@Autowired
	private FastDFSClient fastDFSClient;
	@Autowired
	private MyFriendService myFriendService;
	@Autowired
	private FriendRequestService friendRequestService;

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
		this.add(user);

		String qrcodePath = "D://user" + user.getId() + "qrcode.png";
		// linfq_chat_qrcode:[username]
		qrCodeUtils.createQRCode(qrcodePath, "linfq_chat_qrcode:" + user.getNickname());
		MultipartFile qrcodeFile = FileUtils.fileToMultipart(qrcodePath);

		String qrCodeUrl = "";
		try {
			qrCodeUrl = fastDFSClient.uploadQRCode(qrcodeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		User user4Update = new User();
		user4Update.setId(user.getId());
		user4Update.setQrcode(qrCodeUrl);
		this.update(user4Update);

		user = this.get(user.getId()).orElseThrow(RuntimeException::new);

		return user;
	}

	/**
	 * 通过用户名查询用户
	 *
	 * @param username 用户名
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public Optional<User> getByUsername(String username) {
		User user4Query = new User();
		user4Query.setUsername(username);
		return this.get(user4Query);
	}

	/**
	 * 搜索朋友前置检查.
	 *
	 * @param myUserId
	 * @param friendUsername
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public SearchFriendsStatusEnum preconditionSearchFriends(Integer myUserId, String friendUsername) {
		Optional<User> userOptional = this.getByUsername(friendUsername);

		// 1. 搜索的用户如果不存在，返回[无此用户]
		if (!userOptional.isPresent()) {
			return SearchFriendsStatusEnum.USER_NOT_EXIST;
		}

		User user = userOptional.get();

		// 2. 搜索账号是你自己，返回[不能添加自己]
		if (user.getId().equals(myUserId)) {
			return SearchFriendsStatusEnum.NOT_YOURSELF;
		}

		// 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
		Example mfe = new Example(MyFriend.class);
		Example.Criteria mfc = mfe.createCriteria();
		mfc.andEqualTo("myUserId", myUserId);
		mfc.andEqualTo("myFriendUserId", user.getId());
		Optional<MyFriend> myFriendOptional = this.myFriendService.getByExample(mfe);
		if (myFriendOptional.isPresent()) {
			return SearchFriendsStatusEnum.ALREADY_FRIENDS;
		}

		return SearchFriendsStatusEnum.SUCCESS;
	}

	/**
	 * 发送好友请求.
	 *
	 * @param myUserId
	 * @param friendUsername
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void sendFriendRequest(Integer myUserId, String friendUsername) {
		User friend = this.getByUsername(friendUsername).get();

		// 1. 查询发送好友请求记录表
		Example fre = new Example(FriendRequest.class);
		Example.Criteria frc = fre.createCriteria();
		frc.andEqualTo("sendUserId", myUserId);
		frc.andEqualTo("acceptUserId", friend.getId());
		Optional<FriendRequest> friendRequestOptional = friendRequestService.getByExample(fre);
		if (!friendRequestOptional.isPresent()) {
			// 2. 如果不是你的好友，并且好友记录没有添加，则新增好友请求记录
			FriendRequest request = new FriendRequest();
			request.setSendUserId(myUserId);
			request.setAcceptUserId(friend.getId());
			friendRequestService.add(request);
		}
	}
}
