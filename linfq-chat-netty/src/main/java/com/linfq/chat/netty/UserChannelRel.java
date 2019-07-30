package com.linfq.chat.netty;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * 用户id和channel的关联关系处理.
 *
 * @author linfq
 * @date 2019/7/30 23:19
 */
public class UserChannelRel {

	private static HashMap<Integer, Channel> manager = new HashMap<>();

	public static void put(Integer senderId, Channel channel) {
		manager.put(senderId, channel);
	}

	public static Channel get(Integer senderId) {
		return manager.get(senderId);
	}
}
