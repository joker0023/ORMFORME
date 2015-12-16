package com.joker23.orm.mapping;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.joker23.orm.parser.ParameterParser;

/**
 * 主要用于sql的语句的完善
 * 可将 #{id} 这种转换成 ?
 * 完善PreparedStatement的setXXX
 * @author Joker
 *
 */
public class MappedStatement {

	private Map<String, Object> parameters;
	private String preparedSql;
	private List<String> fieldNames;
	
	/**
	 * 构造方法,初始化 preparedSql 和 fieldNames
	 * @param sql	//类似 insert into user value(#{id}) 这种格式的sql
	 * @param parameters
	 */
	public MappedStatement(String sql, Map<String, Object> parameters){
		this.parameters = parameters;
		fieldNames = new ArrayList<String>();
		this.preparedSql = ParameterParser.parse(sql, parameters, fieldNames);
	}
	
	/**
	 * 返回insert into user value(?, ?) 这种格式的sql
	 * @return
	 */
	public String getPreparedSql(){
		return this.preparedSql;
	}
	
	/**
	 * 返回要setXXX的属性名里列表
	 * @return
	 */
	public List<String> getFieldNames(){
		return this.fieldNames;
	}
	
	/**
	 * 完善PreparedStatement 的 setXXX
	 * @param pstmt
	 * @throws SQLException
	 */
	public void completePreparedStatement(PreparedStatement pstmt) throws SQLException{
		if(null == fieldNames){
			throw new SQLException("fieldNames is null");
		}
		for(int i = 1; i <= fieldNames.size(); i++) {
			pstmt.setObject(i, parameters.get(fieldNames.get(i-1)));
		}
	}
	
	/**
	 * 完善PreparedStatement 的 setXXX
	 * @param pstmt
	 * @param parameters
	 * @throws SQLException
	 */
	public void completePreparedStatement(PreparedStatement pstmt, Map<String, Object> parameters) throws SQLException{
		if(null == fieldNames || null == parameters){
			throw new SQLException("fieldNames or parameters is null");
		}
		for(int i = 1; i <= fieldNames.size(); i++) {
			pstmt.setObject(i, parameters.get(fieldNames.get(i-1)));
		}
	}
	
}
