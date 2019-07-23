package com.linfq.chat.vo.orm;

import lombok.Data;

/**
 * MyFriendResultVo.
 *
 * @author linfq
 * @date 2019/7/23 20:53
 */
@Data
public class MyFriendResultVo {

	private Integer friendUserId;

	private String friendUsername;

	private String friendFaceImage;

	private String friendNickname;
}
