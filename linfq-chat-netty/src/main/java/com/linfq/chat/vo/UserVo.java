package com.linfq.chat.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户.
 *
 * @author linfq
 * @date 2019/7/21 16:32
 */
@Data
public class UserVo {

    /**
     * 主键.
     */
    private Integer id;
    /**
     * 修改时间.
     */
    private LocalDateTime updateTime;
    /**
     * 创建时间.
     */
    private LocalDateTime createTime;
    /**
     * username
     */
    private String username;
    /**
     * face_image
     */
    private String faceImage;

    /**
     * face_image_big
     */
    private String faceImageBig;
    /**
     * nickname
     */
    private String nickname;
    /**
     * qrcode
     */
    private String qrcode;

}