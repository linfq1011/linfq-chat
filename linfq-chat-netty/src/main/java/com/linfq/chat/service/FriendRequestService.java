package com.linfq.chat.service;

import com.alibaba.fastjson.JSON;
import com.linfq.chat.common.constant.MsgActionEnum;
import com.linfq.chat.common.orm.BaseService;
import com.linfq.chat.mapper.FriendRequestMapper;
import com.linfq.chat.model.FriendRequest;
import com.linfq.chat.model.MyFriend;
import com.linfq.chat.netty.DataContentVo;
import com.linfq.chat.netty.UserChannelRel;
import com.linfq.chat.vo.orm.FriendRequestResultVo;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
public class FriendRequestService extends BaseService<FriendRequest> {


	@Autowired
	private FriendRequestMapper friendRequestMapper;
	@Autowired
	private MyFriendService myFriendService;

	/**
	 * 查询好友请求.
	 *
	 * @param acceptUserId
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public List<FriendRequestResultVo> listByAcceptUserId(Integer acceptUserId) {
		return this.friendRequestMapper.selectByAcceptUserId(acceptUserId);
	}

	/**
	 * 删除好友请求
	 *
	 * @param sendUserId
	 * @param acceptUserId
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void delete(Integer sendUserId, Integer acceptUserId) {
		FriendRequest friendRequest4Delete = new FriendRequest();
		friendRequest4Delete.setSendUserId(sendUserId);
		friendRequest4Delete.setAcceptUserId(acceptUserId);
		this.delete(friendRequest4Delete);
	}

	/**
	 * 通过好友申请.
	 *
	 * @param sendUserId
	 * @param acceptUserId
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void passFriendRequest(Integer sendUserId, Integer acceptUserId) {
		// 添加发送方好友关系
		MyFriend myFriendSender = new MyFriend();
		myFriendSender.setMyUserId(sendUserId);
		myFriendSender.setMyFriendUserId(acceptUserId);
		this.myFriendService.add(myFriendSender);
		// 添加接收方好友关系
		MyFriend myFriendAccepter = new MyFriend();
		myFriendAccepter.setMyUserId(acceptUserId);
		myFriendAccepter.setMyFriendUserId(sendUserId);
		this.myFriendService.add(myFriendAccepter);
		// 删除好友请求
		this.delete(sendUserId, acceptUserId);

		// 使用websocket主动推送消息到请求发起者，更新他的通讯列表为最新
		Channel sendChannel = UserChannelRel.get(sendUserId);
		if (sendChannel != null) {
			DataContentVo dataContentVo = new DataContentVo();
			dataContentVo.setAction(MsgActionEnum.PULL_FRIEND.type);

			sendChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(dataContentVo)));
		}
	}

}
