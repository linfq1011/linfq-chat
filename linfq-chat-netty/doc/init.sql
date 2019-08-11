
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `username` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '密码',
  `face_image` varchar(255) NOT NULL DEFAULT '' COMMENT '头像',
  `face_image_big` varchar(255) NOT NULL DEFAULT '' COMMENT '头像大图',
  `nickname` varchar(20) NOT NULL DEFAULT '' COMMENT '昵称',
  `qrcode` varchar(255) NOT NULL DEFAULT '' COMMENT '二维码',
  `cid` varchar(64) NOT NULL DEFAULT '' COMMENT 'client_id',
  PRIMARY KEY (`id`)
) COMMENT='用户表';

CREATE TABLE `friend_request` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `send_user_id` int(10) unsigned NOT NULL COMMENT '发送方用户id',
  `accept_user_id` int(10) unsigned NOT NULL COMMENT '接收方用户id',
  PRIMARY KEY (`id`)
) COMMENT='用户请求表';

CREATE TABLE `my_friend` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `my_user_id` int(10) unsigned NOT NULL COMMENT '我的用户id',
  `my_friend_user_id` int(10) unsigned NOT NULL COMMENT '我的朋友id',
  PRIMARY KEY (`id`)
) COMMENT='好友关系';

CREATE TABLE `chat_msg` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `send_user_id` int(10) unsigned NOT NULL COMMENT '发送者id',
  `accept_user_id` int(10) unsigned NOT NULL COMMENT '接收者id',
  `msg` varchar(255) NOT NULL COMMENT '消息',
  `sign_flag` tinyint(4) unsigned NOT NULL COMMENT '签收状态',
  PRIMARY KEY (`id`)
) COMMENT='聊天记录';
