package com.joker23.orm.executor;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class Executor {

	private static final QueryRunner globalRunner = new QueryRunner();
	
	@SuppressWarnings("rawtypes")
	private final static ColumnListHandler globalColumnListHandler = new ColumnListHandler(){
		@Override
		protected Object handleRow(ResultSet rs) throws SQLException {
			Object obj = super.handleRow(rs);
			if(obj instanceof BigInteger)
				return ((BigInteger)obj).longValue();
			return obj;
		}
		
	};
	
	@SuppressWarnings("rawtypes")
	private final static ScalarHandler globalScaleHandler = new ScalarHandler(){
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if(obj instanceof BigInteger)
				return ((BigInteger)obj).longValue();
			return obj;
		}		
	};
	
	private final static List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>(){
		private static final long serialVersionUID = -2797207611002834866L;

		{
			add(Long.class);
			add(Integer.class);
			add(String.class);
			add(java.util.Date.class);
			add(java.sql.Date.class);
			add(java.sql.Timestamp.class);
	
		}
	};
	
	private final static boolean isPrimitive(Class<?> cls) {
		return cls.isPrimitive() || PrimitiveClasses.contains(cls) ;
	}
	
	/**
	 * 查询单个对象
	 * @param proxoolName
	 * @param beanClass 
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T read(Connection conn, Class<T> beanClass, String sql, Object...params) throws ExecutorException {
		try {
			return (T)globalRunner.query(conn, sql, isPrimitive(beanClass)?globalScaleHandler:new BeanHandler(beanClass), params);
		} catch(Exception e){
			throw new ExecutorException(e);
		} 
	}
	
	/**
	 * 组装成单个对象
	 * @param rs
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T get(ResultSet rs, Class<T> beanClass) throws Exception{
		ResultSetHandler<T> rsh = new BeanHandler<T>(beanClass);
		return rsh.handle(rs);
	}
	
	/**
	 * 查询对象列表
	 * @param <T>
	 * @param proxoolName 
	 * @param beanClass
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> query(Connection conn, Class<T> beanClass, String sql, Object...params) throws ExecutorException {
		try {
			return (List<T>)globalRunner.query(conn, sql, isPrimitive(beanClass)?globalColumnListHandler:new BeanListHandler(beanClass), params);
		} catch(Exception e){
			throw new ExecutorException(e);
		} 
	}
	
	/**
	 * 组装成对象列表
	 * @param rs
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> list(ResultSet rs, Class<T> beanClass) throws Exception{
		ResultSetHandler<T> rsh = (ResultSetHandler<T>)new BeanListHandler<T>(beanClass);
		return (List<T>)rsh.handle(rs);
	}
	
	/**
	 * 执行INSERT/UPDATE/DELETE语句
	 * @param proxoolName
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int update(Connection conn, String sql, Object...params) throws ExecutorException {
		try {
			return globalRunner.update(conn, sql, params);
		} catch(Exception e){
			throw new ExecutorException(e);
		} 
	}
	
}
