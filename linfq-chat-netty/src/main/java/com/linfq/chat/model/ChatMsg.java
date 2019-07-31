package com.linfq.chat.model;

import com.linfq.chat.common.orm.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 聊天内容.
 *
 * @author linfq
 * @date 2019/7/21 16:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChatMsg extends BaseModel {

    /**
     * 发送者id
     * send_user_id
     */
    private Integer sendUserId;

    /**
     * 接收者id
     * accept_user_id
     */
    private Integer acceptUserId;

    /**
     * 消息
     * msg
     */
    private String msg;

    /**
     * 签收状态
     * sign_flag
     */
    private Boolean signFlag;


}