package com.linfq.chat.service;

import com.linfq.chat.common.orm.BaseService;
import com.linfq.chat.common.util.FastDFSClient;
import com.linfq.chat.common.util.FileUtils;
import com.linfq.chat.common.util.QRCodeUtils;
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
}
