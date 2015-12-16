package com.joker23.orm.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import com.joker23.orm.persistence.POJO;

/**
 * 
 * @author Joker
 *
 */
public class BeanUtil {
	
	/**
	 * 此方法返回字段名的 Set(有@Transient注解的忽略,Java 语言修饰符大于等于8的忽略)
	 * @param entity
	 * @param need2Verify 是否验证字段为空
	 * @return
	 */
	public static <T extends POJO> Set<String> getFieldsSet(final T entity, boolean need2Verify) {
		return getFieldsMap(entity, need2Verify).keySet();
	}

	/**
	 * 
	 * 此方法将实体中字段与值组成 Map(有@Transient注解的忽略,Java 语言修饰符大于等于8的忽略)
	 * @param entity 实体
	 * @param need2Verify 是否验证字段为空
	 * @return
	 */
	public static <T extends POJO> Map<String, Object> getFieldsMap(final T entity, boolean need2Verify) {
		Map<String, Object> fieldsMap = new HashMap<String, Object>();
		Map<String, Object> rtnFieldMap = new HashMap<String, Object>();
		
		try {
			Field[] fields = entity.getClass().getDeclaredFields();
			
			Object value = null;
			for(Field field : fields){
				if(!field.isAnnotationPresent(Transient.class) && field.getModifiers() < 8){
					field.setAccessible(true);
					value = field.get(entity);
					fieldsMap.put(field.getName(), value);
				}
			}
			
			rtnFieldMap = fieldsMap;
			if(need2Verify) {
				rtnFieldMap = new HashMap<String, Object>();
				for(String key : fieldsMap.keySet()){
					value = fieldsMap.get(key);
					if(null != value){
						rtnFieldMap.put(key, value);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtnFieldMap;
	}
	
}
