package com.linfq.chat.model;

import com.linfq.chat.common.orm.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户.
 *
 * @author linfq
 * @date 2019/7/21 16:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BaseModel {

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

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

    /**
     * client_id
     * cid
     */
    private String cid;

}