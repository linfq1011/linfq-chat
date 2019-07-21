package com.linfq.chat.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * MyBatisConfig.
 *
 * @author linfq
 * @date 2019/7/21 17:23
 */
@MapperScan("com.linfq.chat.mapper")
@Configuration
public class MyBatisConfig {
}
