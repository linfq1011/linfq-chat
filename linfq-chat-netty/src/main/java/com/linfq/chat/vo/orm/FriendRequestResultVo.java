package com.linfq.chat.vo.orm;

import lombok.Data;

/**
 * 好友请求发送方的信息.
 *
 * @author linfq
 * @date 2019/7/23 18:21
 */
@Data
public class FriendRequestResultVo {

    private Integer sendUserId;

    private String sendUsername;

    private String sendFaceImage;

    private String sendNickname;

}