package com.linfq.chat.mapper;

import com.linfq.chat.common.orm.BaseMapper;
import com.linfq.chat.model.FriendRequest;
import com.linfq.chat.vo.orm.FriendRequestResultVo;

import java.util.List;

/**
 * FriendRequestMapper.
 *
 * @author linfq
 * @date 2019/7/21 16:53
 */
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {

	/**
	 * 查询好友请求.
	 *
	 * @param acceptUserId
	 * @return
	 */
	List<FriendRequestResultVo> selectByAcceptUserId(Integer acceptUserId);
}
