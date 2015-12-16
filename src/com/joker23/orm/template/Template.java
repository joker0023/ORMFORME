package com.joker23.orm.template;

import java.util.List;
import java.util.Set;

import com.joker23.orm.persistence.POJO;
import com.joker23.orm.sql.SQL;
import com.joker23.orm.util.BeanUtil;
import com.joker23.orm.util.TemplateUtil;

/**
 * 动态生成 SQL 的模板类,格式为ID = #{ID} 这种
 * @author Joker
 *
 * @param <T>
 */
public class Template<T extends POJO> {

	/**
	 * 生成 INSERT 语句
	 * @param entity 实体
	 * @return
	 */
	public String save(final T entity) throws TemplateCreateException{
		try{
			if(null == entity){
				throw new TemplateCreateException("Create save Template By Entity Error: Entity is null.");
			}
			
			return new SQL() {{
				INSERT_INTO(entity.tableName());
				VALUES(TemplateUtil.getFieldsStr(entity, true), TemplateUtil.getValsStr(entity, true));
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 批量插入 语句
	 * @param beanClass
	 * @return
	 * @throws TemplateCreateException
	 */
	public String batchSave(Class<? extends POJO> beanClass) throws TemplateCreateException {
		try{
			final POJO entity = beanClass.newInstance();
			
			return new SQL() {{
				INSERT_INTO(entity.tableName());
				VALUES(TemplateUtil.getFieldsStr(entity, false), TemplateUtil.getValsStr(entity, false));
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 忽略唯一索引的批量插入，避免后面的插入无法进行
	 * @param beanClass
	 * @return
	 */
	public String ignoreBatchSave(Class<? extends POJO> beanClass) throws TemplateCreateException {
		try{
			final POJO entity = beanClass.newInstance();
			
			return new SQL() {{
				INSERT_INTO_IGNORE(entity.tableName());
				VALUES(TemplateUtil.getFieldsStr(entity, false), TemplateUtil.getValsStr(entity, false));
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 DELETE 语句
	 * @param beanClass
	 * @param id 主键值
	 * @return
	 */
	public String delete(Class<? extends POJO> beanClass, final long id)
			throws TemplateCreateException {
		try{
			final POJO entity = beanClass.newInstance();
			
			return new SQL() {{
				DELETE_FROM(entity.tableName());
				WHERE(TemplateUtil.getWhereEqualsStr(entity.primaryKey().get(0), String.valueOf(id)));
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 DELETE 语句
	 * @param entity
	 * @param condition
	 * @return
	 */
	public String delete(final T entity, final String condition) throws TemplateCreateException {
		try{
			if(null == entity){
				throw new TemplateCreateException("Create Delete Template By Entity Error: Entity is null.");
			}
			
			return new SQL() {{
				DELETE_FROM(entity.tableName());
				final Set<String> fields = BeanUtil.getFieldsSet(entity, true);
				for(String fieldName : fields) {
					WHERE(TemplateUtil.getWhereEqualsStr(fieldName));
				}
				
				if(null != condition){
					WHERE(condition);
				}
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 DELETE 语句
	 * @param beanClass
	 * @param condition
	 * @return
	 */
	public String delete(final Class<? extends POJO> beanClass, final String condition) throws TemplateCreateException {
		try{
			return new SQL() {{
				DELETE_FROM(beanClass.newInstance().tableName());
				
				if(null != condition){
					WHERE(condition);
				}
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成批量删除操作的语句
	 * @param beanClass
	 * @param ids 主键值
	 * @return
	 */
	public String batchDelete(Class<? extends POJO> beanClass, final List<Long> ids)
			throws TemplateCreateException {
		try{
			final POJO entity = beanClass.newInstance();
			
			return new SQL() {{
				DELETE_FROM(entity.tableName());
				WHERE(TemplateUtil.getWhereInIdsStr(entity, ids));
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 UPDATE 语句,属性值为null不会更新
	 * @param entity 实体
	 * @return
	 */
	public String update(final T entity) throws TemplateCreateException {
		return update(entity, true);
	}
	
	/**
	 * 生成 UPDATE 语句,属性值为null也会更新
	 * @param entity
	 * @return
	 * @throws TemplateCreateException
	 */
	public String enforceUpdate(final T entity) throws TemplateCreateException {
		return update(entity, false);
	}
	
	/**
	 * 生成 BATCHUPDATE 语句,属性值为null也会更新
	 * @param beanClass
	 * @return
	 * @throws TemplateCreateException
	 */
	public String batchUpdate(final Class<? extends POJO> beanClass) throws TemplateCreateException {
		try{
			return update(beanClass.newInstance(), false);
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	private <V extends POJO> String update(final V entity, final boolean need2Verify) throws TemplateCreateException {
		try{
			if(null == entity){
				throw new TemplateCreateException("Create Update Template By Entity Error: Entity is null.");
			}
			
			final Set<String> fields = BeanUtil.getFieldsSet(entity, need2Verify);
			return new SQL() {{
				UPDATE(entity.tableName());
				for(String fieldName : fields) {
					SET(TemplateUtil.getSetStr(fieldName));
				}
				
				List<String> pkList = entity.primaryKey();
				for(String pk : pkList){
					WHERE(TemplateUtil.getWhereEqualsStr(pk));
				}
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 SELECT ONE 语句
	 * @param beanClass 实体类
	 * @param id 主键值
	 * @param fieldNames 指定返回的字段，不指定返回全部字段值
	 * @return
	 */
	public String get(Class<? extends POJO> beanClass, final long id,
			final String... fieldNames) throws TemplateCreateException {
		try{
			final POJO entity = beanClass.newInstance();
			
			return new SQL() {{
				if(null != fieldNames && 0 < fieldNames.length) {
					SELECT(TemplateUtil.getFieldsStr(fieldNames));
				} else {
					SELECT(TemplateUtil.getFieldsStr(entity, false));
				}
				
				FROM(entity.tableName());
				WHERE(TemplateUtil.getWhereEqualsStr(entity.primaryKey().get(0), String.valueOf(id)));
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 SELECT ONE 语句
	 * @param entity 实体
	 * @param fieldNames 指定返回的字段，不指定返回全部字段值
	 * @return
	 */
	public String get(final T entity, final String... fieldNames)
			throws TemplateCreateException {
		try{
			if(null == entity){
				throw new TemplateCreateException("Create Get Template By Entity Error: Entity is null.");
			}
			
			String sql = new SQL() {{
				if(null != fieldNames && 0 < fieldNames.length) {
					SELECT(TemplateUtil.getFieldsStr(fieldNames));
				} else {
					SELECT(TemplateUtil.getFieldsStr(entity, false));
				}
				
				FROM(entity.tableName());
				
				final Set<String> fields = BeanUtil.getFieldsSet(entity, true);
				for(String fieldName : fields) {
					WHERE(TemplateUtil.getWhereEqualsStr(fieldName));
				}
			}}.toString();
			
			sql += TemplateUtil.getLimitStr(0, 1);
			
			return sql;
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 SELECT LIST 语句
	 * @param beanClass 实体类
	 * @param ids 主键值列表
	 * @param fieldNames 指定返回的字段，不指定返回全部字段值
	 * @return
	 */
	public String list(Class<? extends POJO> beanClass, final List<Long> ids,
			final String... fieldNames) throws TemplateCreateException {
		try{
			final POJO entity = beanClass.newInstance();
			
			return new SQL() {{
				if(null != fieldNames && 0 < fieldNames.length) {
					SELECT(TemplateUtil.getFieldsStr(fieldNames));
				} else {
					SELECT(TemplateUtil.getFieldsStr(entity, false));
				}
				
				FROM(entity.tableName());
				WHERE(TemplateUtil.getWhereInIdsStr(entity, ids));
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成分页的 SELECT LIST 语句
	 * @param entity 实体
	 * @param page 页数
	 * @param size 每页的条目数
	 * @param fieldNames 指定返回的字段，不指定返回全部字段值
	 * @return
	 */
	public String list(final T entity, final Integer page, final Integer size,
			final String... fieldNames) throws TemplateCreateException {
		try{
			if(null == entity){
				throw new TemplateCreateException("Create List Template By Entity Error: Entity is null.");
			}
			
			String sql = new SQL(){{
				if(null != fieldNames && fieldNames.length > 0){
					SELECT(TemplateUtil.getFieldsStr(fieldNames));
				}else{
					SELECT(TemplateUtil.getFieldsStr(entity, false));
				}
				FROM(entity.tableName());
				final Set<String> fields = BeanUtil.getFieldsSet(entity, true);
				for(String fieldName : fields){
					WHERE(TemplateUtil.getWhereEqualsStr(fieldName));
				}
			}}.toString();
			
			if(null != page && null != size && page > 0 && size > 0) {
				sql += TemplateUtil.getLimitStr((page-1)*size, size);
			}
			
			return sql;
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 SELECT LIST ALL语句
	 * @param beanClass
	 * @return
	 * @throws TemplateCreateException
	 */
	public String listAll(final Class<? extends POJO> beanClass) throws TemplateCreateException {
		try{
			String sql = new SQL(){{
				POJO pojo = beanClass.newInstance();
				SELECT(TemplateUtil.getFieldsStr(pojo, false));
				FROM(pojo.tableName());
			}}.toString();
			
			return sql;
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成分页条件查询的 SELECT LIST 语句
	 * @param entity 实体
	 * @param condition 条件
	 * @param page 页数
	 * @param size 每页的条目数
	 * @param orderByStr	排序
	 * @param fieldNames 指定返回的字段，不指定返回全部字段值
	 * @return
	 */
	public String filter(final T entity, final String condition, final Integer page,
			final Integer size, final String orderByStr, final String... fieldNames)
			throws TemplateCreateException {
		try{
			if(null == entity){
				throw new TemplateCreateException("Create Filter Template By Entity Error: Entity is null.");
			}
			
			String sql = new SQL(){{
				if(null != fieldNames && fieldNames.length > 0){
					SELECT(TemplateUtil.getFieldsStr(fieldNames));
				}else{
					SELECT(TemplateUtil.getFieldsStr(entity, false));
				}
				
				FROM(entity.tableName());
				
				final Set<String> fields = BeanUtil.getFieldsSet(entity, true);
				for(String fieldName : fields) {
					WHERE(TemplateUtil.getWhereEqualsStr(fieldName));
				}
				
				if(null != condition) {
					WHERE(condition);
				}
				
				if(null != orderByStr){
					ORDER_BY(orderByStr);
				}
			}}.toString();
			
			if(null != page && null != size && page > 0 && size > 0) {
				sql += TemplateUtil.getLimitStr((page-1) * size, size);
			}
			
			return sql;
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成分页条件查询的 SELECT LIST 语句
	 * @param beanClass
	 * @param condition	条件
	 * @param page
	 * @param size
	 * @param orderByStr	排序
	 * @param fieldNames	指定返回的字段，不指定返回全部字段值
	 * @return
	 * @throws TemplateCreateException
	 */
	public String filter(final Class<? extends POJO> beanClass, final String condition, final Integer page,
			final Integer size, final String orderByStr, final String... fieldNames)
			throws TemplateCreateException {
		try{
			String sql = new SQL(){{
				POJO pojo = beanClass.newInstance();
				if(null != fieldNames && fieldNames.length > 0){
					SELECT(TemplateUtil.getFieldsStr(fieldNames));
				}else{
					SELECT(TemplateUtil.getFieldsStr(pojo, false));
				}
				
				FROM(pojo.tableName());
				
				if(null != condition) {
					WHERE(condition);
				}
				
				if(null != orderByStr){
					ORDER_BY(orderByStr);
				}
			}}.toString();
			
			if(null != page && null != size && page > 0 && size > 0) {
				sql += TemplateUtil.getLimitStr((page-1) * size, size);
			}
			
			return sql;
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 COUNT 语句
	 * @param entity	实体
	 * @param condition	自定义条件
	 * @return
	 */
	public String count(final T entity, final String condition)
			throws TemplateCreateException {
		try{
			if(null == entity){
				throw new TemplateCreateException("Create Count Template By Entity Error: Entity is null.");
			}
			
			return new SQL() {{
				SELECT(TemplateUtil.getCountStr(entity.primaryKey().get(0)));
				FROM(entity.tableName());
				
				final Set<String> fields = BeanUtil.getFieldsSet(entity, true);
				for(String fieldName : fields) {
					WHERE(TemplateUtil.getWhereEqualsStr(fieldName));
				}
				
				if(null != condition){
					WHERE(condition);
				}
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
	/**
	 * 生成 COUNT 语句
	 * @param beanClass
	 * @param condition	自定义条件
	 * @return
	 */
	public String count(final Class<? extends POJO> beanClass, final String condition)
			throws TemplateCreateException {
		try{
			return new SQL() {{
				final POJO pojo = beanClass.newInstance();
				SELECT(TemplateUtil.getCountStr(pojo.primaryKey().get(0)));
				FROM(pojo.tableName());
				
				if(null != condition){
					WHERE(condition);
				}
			}}.toString();
		}catch (Exception e) {
			throw new TemplateCreateException(e);
		}
	}
	
}
