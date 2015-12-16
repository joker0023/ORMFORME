package com.joker23.orm.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.joker23.orm.persistence.POJO;

public class GenerateSqlUtil {

	public static <T extends POJO> String generateSQL(T entity){
		StringBuilder sql = new StringBuilder();
		try {
			sql.append("CREATE TABLE `");
			sql.append(entity.tableName());
			sql.append("` (");
			Field[] fields = entity.getClass().getDeclaredFields();
			List<Field> keyList = new ArrayList<Field>();
			
			String sqlType = "";
			for(Field field : fields){
				if(!field.isAnnotationPresent(Transient.class) && field.getModifiers() < 8){
					sqlType = getSQLType(field.getGenericType().toString());
					sql.append("`");
					sql.append(field.getName().toLowerCase());
					sql.append("` ");
					sql.append(sqlType);
					
					if(field.isAnnotationPresent(Id.class)){
						keyList.add(field);
						sql.append(" NOT NULL");
						if(keyList.size() == 1){
							sql.append(" AUTO_INCREMENT");
						}
					}else{
						if(sqlType.contains("varchar")){
							sql.append(" COLLATE utf8_bin");
						}
						sql.append(" DEFAULT NULL");
					}
					sql.append(",");
				}
			}
			sql.append(" PRIMARY KEY (");
			for(int i = 0; i < keyList.size(); i++){
				Field key = keyList.get(i);
				if(i > 0){
					sql.append(",");
				}
				sql.append("`");
				sql.append(key.getName().toLowerCase());
				sql.append("`");
			}
			sql.append(")) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return sql.toString();
	}
	
	private static String getSQLType(String type){
		if("class java.lang.Long".equals(type.toString())){
			return "bigint(20)";
		}else if("class java.lang.String".equals(type.toString())){
			return "varchar(50)";
		}else if("class java.lang.Integer".equals(type.toString())){
			return "int(11)";
		}else if("class java.lang.Double".equals(type.toString())){
			return "double";
		}else if("class java.lang.Float".equals(type.toString())){
			return "float";
		}else if("class java.lang.Boolean".equals(type.toString())){
			return "tinyint(4)";
		}else if("class java.lang.Byte".equals(type.toString())){
			return "tinyint(4)";
		}else if("class java.util.Date".equals(type.toString())){
			return "datetime";
		}
		
		return null;
	}
	
}
