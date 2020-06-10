CREATE TABLE user (
    user_id int(4) NOT NULL AUTO_INCREMENT COMMENT '用户id',
    username varchar(63) NOT NULL DEFAULT ' ' COMMENT '用户名',
    password varchar(63) NOT NULL DEFAULT ' ' COMMENT '密码',
    PRIMARY KEY (user_id),
    UNIQUE KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE favorite_video (
    user_id int(4) NOT NULL COMMENT '用户id',
    video_id varchar(63) NOT NULL DEFAULT ' ' COMMENT '收藏的视频id',
    PRIMARY KEY (user_id, video_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频收藏表';