package com.linfq.chat.model;

import com.linfq.chat.common.orm.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 好友请求
 *
 * @author linfq
 * @date 2019/7/21 16:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FriendRequest extends BaseModel {

    /**
     * send_user_id
     */
    private Integer sendUserId;

    /**
     * accept_user_id
     */
    private Integer acceptUserId;

}