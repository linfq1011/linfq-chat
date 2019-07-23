package com.linfq.chat.mapper;

import com.linfq.chat.common.orm.BaseMapper;
import com.linfq.chat.model.MyFriend;
import com.linfq.chat.vo.orm.MyFriendResultVo;

import java.util.List;

/**
 * MyFriendMapper.
 *
 * @author linfq
 * @date 2019/7/21 16:53
 */
public interface MyFriendMapper extends BaseMapper<MyFriend> {

	/**
	 * 查询我的好友.
	 *
	 * @param userId
	 * @return
	 */
	List<MyFriendResultVo> selectMyFriends(Integer userId);
}
