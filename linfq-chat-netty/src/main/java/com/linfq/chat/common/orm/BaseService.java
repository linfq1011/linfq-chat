package com.linfq.chat.common.orm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * BaseService.
 *
 * @author linfq
 * @date 2019/7/21 17:26
 */
@Service
public abstract class BaseService<T extends BaseModel> {

	/**
	 * 泛型注入mapper.
	 */
	@Autowired
	private BaseMapper<T> mapper;
	/**
	 * 当前泛型的Class.
	 */
	private Class<T> modelClass;

	/**
	 * 构造函数.
	 */
	@SuppressWarnings("unchecked")
	public BaseService() {
		// 获得具体model，通过反射来根据属性条件查找数据
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		modelClass = (Class<T>) pt.getActualTypeArguments()[0];
	}

	/**
	 * 创建实体对象.
	 *
	 * @return model实例
	 */
	private T createModel() {
		try {
			return modelClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("实例化实体对象异常");
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("实例化实体对象异常，默认构造方法没有访问权限");
		}
	}

	/**
	 * 查询一条.
	 *
	 * @param id 主键
	 *
	 * @return 实体
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public Optional<T> get(Integer id) {
		T t = this.createModel();
		t.setId(id);
		return Optional.ofNullable(this.mapper.selectByPrimaryKey(t));
	}

	/**
	 * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号.
	 *
	 * @param t 实体（查询条件）
	 * @return 实体（结果）
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public Optional<T> get(T t) {
		return Optional.ofNullable(this.mapper.selectOne(t));
	}

	/**
	 * 根据example查询一个，有多个结果是抛出异常.
	 *
	 * @param example 条件
	 * @return 实体
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public Optional<T> getByExample(Example example) {
		return Optional.ofNullable(this.mapper.selectOneByExample(example));
	}

	/**
	 * 添加一条.
	 *
	 * @param t 实体
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void add(T t) {
		this.mapper.insertSelective(t);
	}

	/**
	 * 修改一条（忽略为null的字段）.
	 *
	 * @param t 实体
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void update(T t) {
		if (t.getId() != null) {
			this.mapper.updateByPrimaryKeySelective(t);
		}
	}

	/**
	 * 保存一条（忽略为null的字段），返回持久化对象.
	 *
	 * @param t
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public T save(T t) {
		this.update(t);
		return this.mapper.selectByPrimaryKey(t.getId());
	}

	/**
	 * 删除一条.
	 *
	 * @param id 主键
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void delete(Integer id) {
		T t = this.createModel();
		t.setId(id);
		this.mapper.deleteByPrimaryKey(id);
	}

	/**
	 * 查询全部.
	 *
	 * @return 集合
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public List<T> listAll() {
		return this.mapper.selectAll();
	}

	/**
	 * example查询列表.
	 *
	 * @param example 条件
	 * @return 集合
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public List<T> listByExample(Example example) {
		return this.mapper.selectByExample(example);
	}

	/**
	 * example查询数量.
	 *
	 * @param example 条件
	 * @return 数量
	 */
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor=Exception.class)
	public int countByExample(Example example) {
		return this.mapper.selectCountByExample(example);
	}

}
