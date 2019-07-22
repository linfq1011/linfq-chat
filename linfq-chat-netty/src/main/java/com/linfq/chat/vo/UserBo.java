package com.linfq.chat.vo;

import lombok.Data;

/**
 * 用户.
 *
 * @author linfq
 * @date 2019/7/22 22:52
 */
@Data
public class UserBo {

    /**
     * 用户id.
     */
    private Integer userId;
    /**
     * 用户头像Base64.
     */
    private String faceData;

}