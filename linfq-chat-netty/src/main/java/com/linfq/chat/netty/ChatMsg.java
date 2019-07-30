package com.linfq.chat.netty;

import lombok.Data;

import java.io.Serializable;

/**
 * ChatMsg.
 *
 * @author linfq
 * @date 2019/7/30 23:07
 */
@Data
public class ChatMsg implements Serializable {

	/**
	 * 发送者的用户id.
	 */
	private Integer senderId;
	/**
	 * 接收者的用户id.
	 */
	private Integer receiverId;
	/**
	 * 聊天内容.
	 */
	private String msg;
	/**
	 * 用于消息签收.
	 */
	private Integer msgId;


}
