package com.linfq.chat.service;

import com.linfq.chat.common.orm.BaseService;
import com.linfq.chat.mapper.MyFriendMapper;
import com.linfq.chat.model.MyFriend;
import com.linfq.chat.vo.orm.MyFriendResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * MyFriendService.
 *
 * @author linfq
 * @date 2019/7/23 11:30
 */
@Service
public class MyFriendService extends BaseService<MyFriend> {

	@Autowired
	private MyFriendMapper myFriendMapper;

	/**
	 * 查询我的好友列列表.
	 *
	 * @param userId
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public List<MyFriendResultVo> listMyFriends(Integer userId) {
		return this.myFriendMapper.selectMyFriends(userId);
	}
}
