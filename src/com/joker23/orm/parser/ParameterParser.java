package com.joker23.orm.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql的转换
 * @author Joker
 *
 */
public class ParameterParser {
	private static final String startToken = "#\\{";
	private static final String endToken = "\\}";
	private static final String regex = startToken + "[\\$\\w]+" + endToken;

	/**
	 * 返回insert into user value(?, ?) 这种格式的sql
	 * @param sql
	 * @param variables
	 * @param fieldNames
	 * @return
	 */
	public static String parse(String sql, Map<String, Object> variables, List<String> fieldNames) {
		if(null == sql || null == variables){
			return sql;
		}
		
		if(null == fieldNames){
			fieldNames = new ArrayList<String>();
		}
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sql);
		
		String matcherStr = null;
		String fieldName = null;
		while(matcher.find()){
			matcherStr = matcher.group();
			if(matcherStr.length() > 2){
				fieldName = matcherStr.substring(2, matcherStr.length() - 1);
				if(variables.containsKey(fieldName)){
					fieldNames.add(fieldName);
					sql = sql.replaceFirst(startToken + fieldName + endToken, "?");
					matcher = pattern.matcher(sql);
				}
			}
		}
		
		return sql;
	}
	
}
