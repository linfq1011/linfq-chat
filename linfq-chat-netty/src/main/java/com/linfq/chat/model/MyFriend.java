package com.linfq.chat.model;

import com.linfq.chat.common.orm.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 我的好友.
 *
 * @author linfq
 * @date 2019/7/21 16:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MyFriend extends BaseModel {

    /**
     * my_user_id
     */
    private Integer myUserId;

    /**
     * my_friend_user_id
     */
    private Integer myFriendUserId;

}