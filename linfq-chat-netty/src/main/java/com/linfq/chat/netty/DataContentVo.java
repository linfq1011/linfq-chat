package com.linfq.chat.netty;

import lombok.Data;

import java.io.Serializable;

/**
 * DataContentVo.
 *
 * @author linfq
 * @date 2019/7/30 23:06
 */
@Data
public class DataContentVo implements Serializable {

	/**
	 * 动作类型.
	 */
	private Integer action;
	/**
	 * 用户聊天内容.
	 */
	private ChatMsgVo chatMsg;
	/**
	 * 扩展字段.
	 */
	private String extand;
}
