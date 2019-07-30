package com.linfq.chat.netty;

import lombok.Data;

import java.io.Serializable;

/**
 * DataContent.
 *
 * @author linfq
 * @date 2019/7/30 23:06
 */
@Data
public class DataContent implements Serializable {

	/**
	 * 动作类型.
	 */
	private Integer action;
	/**
	 * 用户聊天内容.
	 */
	private ChatMsg chatMsg;
	/**
	 * 扩展字段.
	 */
	private String extand;
}
