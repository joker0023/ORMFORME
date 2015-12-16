package com.joker23.orm.persistence;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 表对象
 * 数据库对象可继承此对象,属性对应数据库的列(有@Transient注解的忽略,Java 语言修饰符大于等于8的忽略) <br/>
 * 继承对象应有Table注解,name='表名',主键要用Id注解,不对应数据库列的属性用Transient注解,
 * 语言修饰符大于等于8默认不会对应数据库列,例如用static修饰的属性不对应数据库列
 * @author Joker
 *
 */
public class POJO implements Serializable {

	private static final long serialVersionUID = 4225404974278613226L;

	/**
	 * 获取 POJO 对应的表名 
     * 需要 POJO 中的属性定义 @Table(name) 
	 * @return
	 */
	public String tableName() {
		Table table = this.getClass().getAnnotation(Table.class);  
        if(null != table) {
        	return table.name();  
        } else {
            throw new RuntimeException("Undefined POJO @Table");
        }
    }  
	
	/** 
     * 获取 POJO 对应的主键 PrimaryKey 的名称 
     * 需要 POJO 中的属性定义 @Id 
     * @return 
     */  
    public List<String> primaryKey() {
    	List<String> pkList = new ArrayList<String>();
    	for(Field field : this.getClass().getDeclaredFields()) {  
            if(field.isAnnotationPresent(Id.class)) {
            	pkList.add(field.getName());
            }
    	}
    	if(pkList.size() < 1) {
        	throw new RuntimeException("Undefined POJO @Id");
        }
    	
        return pkList;
    }  

}
