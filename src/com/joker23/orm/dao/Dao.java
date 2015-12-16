package com.joker23.orm.dao;

import java.sql.SQLException;
import java.util.List;

import com.joker23.orm.persistence.POJO;

/**
 * 
 * @author Joker
 *
 * @param <T>
 */
public interface Dao<T extends POJO> {

	/**
	 * 插入操作
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	long save(T entity) throws SQLException;
	
	/**
	 * 批量插入操作
	 * @param entitylist
	 * @return
	 * @throws SQLException
	 */
	int batchSave(List<T> entitylist) throws SQLException;
	
	/**
	 * ignore批量插入操作
	 * @param entitylist
	 * @return
	 * @throws SQLException
	 */
	int ignoreBatchSave(List<T> entitylist) throws SQLException;
	
	/**
	 * 删除操作
	 * @param id	主键
	 * @return
	 * @throws SQLException
	 */
	int delete(long id) throws SQLException;
	
	
	/**
	 * 删除操作
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	int delete(T entity) throws SQLException;
	
	/**
	 * 删除操作
	 * @param condition	自定义条件
	 * @return
	 * @throws SQLException
	 */
	int delete(String condition) throws SQLException;
	
	/**
	 * 删除操作
	 * @param entity
	 * @param condition	自定义条件
	 * @return
	 * @throws SQLException
	 */
	int delete(T entity, String condition) throws SQLException;
	
	/**
	 * 批量删除操作
	 * @param ids	主键串
	 * @return
	 * @throws SQLException
	 */
	int batchDelete(List<Long> ids) throws SQLException;
	
	/**
	 * 更新操作,属性值为null不会更新
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	int update(T entity) throws SQLException;
	
	/**
	 * 更新操作,属性值为null也会更新
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	int enforceUpdate(T entity) throws SQLException;
	
	/**
	 * 批量更新操作,属性值为null也会更新
	 * @param entitylist
	 * @return
	 * @throws SQLException
	 */
	int batchUpdate(List<T> entitylist) throws SQLException;
	
	/**
	 * 单个查询操作
	 * @param id			主键
	 * @param fieldNames	指定返回的字段，不指定返回全部字段值
	 * @return
	 * @throws SQLException
	 */
	T get(long id, String...fieldNames) throws SQLException;
	
	/**
	 * 单个查询操作
	 * @param entity
	 * @param fieldNames	指定返回的字段，不指定返回全部字段值
	 * @return
	 * @throws SQLException
	 */
	T get(T entity, String...fieldNames) throws SQLException;
	
	/**
	 * 列表查询操作
	 * @param ids			主键串
	 * @param fieldNames	指定返回的字段，不指定返回全部字段值
	 * @return
	 * @throws SQLException
	 */
	List<T> list(List<Long> ids, String...fieldNames) throws SQLException;
	
	/**
	 * 列表查询操作
	 * @param entity
	 * @param page			分页,页码
	 * @param size			分页,每页size
	 * @param fieldNames	指定返回的字段，不指定返回全部字段值
	 * @return
	 * @throws SQLException
	 */
	List<T> list(T entity, Integer page, Integer size, String...fieldNames) throws SQLException;
	
	/**
	 * 查询所有
	 * @return
	 * @throws SQLException
	 */
	List<T> listAll() throws SQLException;
	
	/**
	 * 列表查询操作
	 * @param entity
	 * @param condition	自定义查询条件
	 * @param page		分页,页码
	 * @param size		分页,每页size
	 * @param orderBy		排序字段,类似"id desc,cid asc" 这样
	 * @param fieldNames	指定返回的字段，不指定返回全部字段值
	 * @return
	 * @throws SQLException
	 */
	List<T> filter(T entity, String condition, Integer page, Integer size, String orderBy, String...fieldNames) throws SQLException;

	/**
	 * 列表查询操作
	 * @param condition	自定义查询条件
	 * @param page		分页,页码
	 * @param size		分页,每页size
	 * @param orderBy		排序字段,类似"id desc,cid asc" 这样
	 * @param fieldNames	指定返回的字段，不指定返回全部字段值
	 * @return
	 * @throws SQLException
	 */
	List<T> filter(String condition, Integer page, Integer size, String orderBy, String...fieldNames) throws SQLException;
	
	/**
	 * 数目查询操作
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	long count(T entity) throws SQLException;
	
	/**
	 * 数目查询操作
	 * @param entity
	 * @param condition	自定义查询条件
	 * @return
	 * @throws SQLException
	 */
	long count(T entity, String condition) throws SQLException;
	
	/**
	 * 数目查询操作
	 * @param condition	自定义查询条件
	 * @return
	 * @throws SQLException
	 */
	long count(String condition) throws SQLException;
	
	/**
	 * 执行SQL语句
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	int excute(String sql) throws SQLException;
	
	/**
	 * 执行sql
	 * @param sql
	 * @param param	参数
	 * @return
	 * @throws SQLException
	 */
	List<T> query(String sql, Object...params) throws SQLException;
	
	/**
	 * 执行SQL查询语句
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	List<List<Object>> executeQuery(String sql) throws SQLException;
	
}
