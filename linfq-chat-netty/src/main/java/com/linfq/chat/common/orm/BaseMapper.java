package com.linfq.chat.common.orm;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用mapper.
 *
 * @param <T> 实体泛型
 *
 * @author linfq
 * @date 2019/7/21 16:49
 */
public interface BaseMapper<T extends BaseModel> extends Mapper<T>, ConditionMapper<T>, IdsMapper<T>, MySqlMapper<T> {

}
