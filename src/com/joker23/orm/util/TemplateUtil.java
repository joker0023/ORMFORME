package com.joker23.orm.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.joker23.orm.persistence.POJO;

/**
 * 
 * @author Joker
 *
 */
public class TemplateUtil {
	
	/**
	 * 获取字段的变量名形式的字符串，例如：id => "#{id}"
	 * @param fieldName 字段名
	 * @return
	 */
	public static String getFieldValStr(String fieldName) {
		return String.format("#{%s}", fieldName);
	}
	
	/**
	 * 获取字段作为 WHERE 相等的字符串，例如：id => "ID = #{id}"
	 * @param fieldName 字段名
	 * @return
	 */
	public static String getWhereEqualsStr(String fieldName) {
		return String.format("%s = #{%s}", fieldName.toUpperCase(), fieldName);
	}
	
	/**
	 * 获取字段作为 WHERE 相等的字符串，例如：id => "ID = 123"
	 * @param fieldName 字段名
	 * @param val 字段的值
	 * @return
	 */
	public static String getWhereEqualsStr(String fieldName, String val) {
		return String.format("%s = %s", fieldName.toUpperCase(), val);
	}
	
	/**
	 * 获取字段作为 SET 的字符串，例如：id => "ID = #{id}"
	 * @param fieldName 字段名
	 * @return
	 */
	public static String getSetStr(String fieldName) {
		return String.format("%s = #{%s}", fieldName.toUpperCase(), fieldName);
	}

	/**
	 * 返回 LIMIT 的字符串，例如：0, 1 => "LIMIT 0, 1"
	 * @param start 起始下标
	 * @param size 获取的条数
	 * @return
	 */
	public static String getLimitStr(Integer start, Integer size) {
		return String.format(" LIMIT %d, %d", start, size);
	}
	
	/**
	 * 返回 COUNT 的字符串，例如："id" => "COUNT(ID)"
	 * @param fieldName 计数的字段
	 * @return
	 */
	public static String getCountStr(String fieldName) {
		return String.format("COUNT(%s)", fieldName.toUpperCase());
	}
	
	/**
	 * 此方法返回实体中字段名字符串，例如：USERNAME, PASSWD
	 * @param entity 实体
	 * @param need2Verify 是否验证字段为空
	 * @return
	 */
	public static <T extends POJO> String getFieldsStr(T entity, boolean need2Verify) {
		Map<String, Object> fieldsMap = BeanUtil.getFieldsMap(entity, need2Verify);
		return getFieldsStr(fieldsMap.keySet().toArray());
	}
	
	/**
	 * 此方法将字段名数组拼接，例如：[username, passwd] => "USERNAME, PASSWD"
	 * @param fieldNames
	 * @return
	 */
	public static String getFieldsStr(Object[] fieldNames) {
		StringBuilder fieldsStr = new StringBuilder();
		
		for (Object fieldName : fieldNames) {
			fieldsStr.append(fieldName.toString().toUpperCase());
			fieldsStr.append(", ");
		}
		
		if(fieldsStr.length() > 2) {
			fieldsStr.delete(fieldsStr.length()-2, fieldsStr.length()-1);
		}
		
		return fieldsStr.toString();
	}
	
	/**
	 * 此方法返回实体中字段的变量形式的字符串，例如：#{username}, #{passwd} 
	 * @param entity
	 * @param need2Verify 是否验证字段为空
	 * @return
	 */
	public static <T extends POJO> String getValsStr(T entity, boolean need2Verify) {
		StringBuilder valsStr = new StringBuilder();
		
		Map<String, Object> fieldsMap = BeanUtil.getFieldsMap(entity, need2Verify);
		
		for(Entry<String, Object> fieldEntry : fieldsMap.entrySet()) {
			valsStr.append("#{");
			valsStr.append(fieldEntry.getKey());
			valsStr.append("}, ");
		}
		
		if(valsStr.length() > 2) {
			valsStr.delete(valsStr.length()-2, valsStr.length()-1);
		}
		
		return valsStr.toString();
	}
	
	/**
	 * 获取where条件 例如传入一个id串 得到 : ID IN (xx,xx,xx)
	 * @param entity
	 * @param ids
	 * @return
	 */
	public static <T extends POJO> String getWhereInIdsStr(T entity, List<Long> ids){
		return getWhereInIdsStr(entity.primaryKey().get(0), ids);
	}
	
	/**
	 * 获取where条件 例如传入一个id串 得到 : fieldStr IN (xx,xx,xx)
	 * @param fieldStr
	 * @param ids
	 * @return
	 */
	public static <T extends POJO> String getWhereInIdsStr(String fieldStr, List<Long> ids){
		StringBuilder whereStr = new StringBuilder();
		if(null != ids && ids.size() > 0) {
			whereStr.append(fieldStr.toUpperCase() + " IN (");
			int size = ids.size();
			for(int i = 0; i < size; i++){
				if(i > 0) {
					whereStr.append(",");
				}
				whereStr.append(ids.get(i));
			}
			whereStr.append(") ");
		}
		return whereStr.toString();
	}
	
}
