package com.linfq.chat.service;

import com.linfq.chat.common.orm.BaseService;
import com.linfq.chat.mapper.ChatMsgMapper;
import com.linfq.chat.model.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

/**
 * 聊天消息Service.
 *
 * @author linfq
 * @date 2019/7/31 20:36
 */
@Service
public class ChatMsgService extends BaseService<ChatMsg> {

	@Autowired
	private ChatMsgMapper mapper;

	/**
	 * 批量更新消息为已签收.
	 *
	 * @param ids
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public void updateMsgSigned(List<Integer> ids) {
		ChatMsg chatMsg4Update = new ChatMsg();
		chatMsg4Update.setSignFlag(Boolean.TRUE);
		Weekend<ChatMsg> weekend = Weekend.of(ChatMsg.class);
		WeekendCriteria<ChatMsg, Object> criteria = weekend.weekendCriteria();
		criteria.andIn(ChatMsg::getId, ids);
		this.mapper.updateByExampleSelective(chatMsg4Update, weekend);
	}
}
