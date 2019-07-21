package com.linfq.chat.common.orm;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类.
 *
 * @author linfq
 * @date 2019/7/21 16:48
 */
@Data
public class BaseModel implements Serializable {

	/**
	 * 主键.
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	private Integer id;

	/**
	 * 修改时间.
	 */
	@Column(insertable = false, updatable = false)
	private LocalDateTime updateTime;

	/**
	 * 创建时间.
	 */
	@Column(insertable = false, updatable = false)
	private LocalDateTime createTime;

}
