package com.linfq.chat.model;

import com.linfq.chat.common.orm.BaseModel;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table chat_msg
 */
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
    private Byte signFlag;


}