package com.joker23.orm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.joker23.orm.connection.ConnectionFactory;
import com.joker23.orm.dao.Dao;
import com.joker23.orm.executor.Executor;
import com.joker23.orm.mapping.MappedStatement;
import com.joker23.orm.persistence.POJO;
import com.joker23.orm.template.Template;
import com.joker23.orm.util.BeanUtil;

/**
 * 
 * @author Joker
 * dao 的实现类
 * 
 * @param <T>
 */
public class BaseDao<T extends POJO> implements Dao<T>{
	
	private static Log log = LogFactory.getLog(BaseDao.class);
	
	private String dsAlias;
	private Class<T> beanClass;
	private Template<T> template;
	private int batchLimit = 500;
	
	public BaseDao(Class<T> beanClass, String dsAlias) {
		this.dsAlias = dsAlias;
		this.beanClass = beanClass;
		template = new Template<T>();
	}

	public int getBatchLimit() {
		return batchLimit;
	}

	public void setBatchLimit(int batchLimit) {
		this.batchLimit = batchLimit;
	}

	@Override
	public long save(T entity) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long result = -1;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.save(entity);
			
			MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity, true));
			String preparedSql = mappedStatement.getPreparedSql();
			
			log.debug("sql: " + sql);
			log.debug("preparedSql: " + preparedSql);
			
			pstmt = conn.prepareStatement(preparedSql, PreparedStatement.RETURN_GENERATED_KEYS);
			mappedStatement.completePreparedStatement(pstmt);
			
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			
			result = rs.next()?rs.getLong(1):-1;
		} finally {
			ConnectionFactory.close(rs);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
		
		return result;
	}

	@Override
	public int batchSave(List<T> entitylist) throws SQLException {
		return batchSave2(entitylist, false);
	}

	@Override
	public int ignoreBatchSave(List<T> entitylist) throws SQLException {
		return batchSave2(entitylist, true);
	}
	
	private int batchSave2(List<T> entitylist, boolean ignore) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = -1;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			conn.setAutoCommit(false);
			
			if(null != entitylist && entitylist.size() > 0){
				T entity0 = entitylist.get(0);
				String sql = null;
				if(ignore){
					sql = template.ignoreBatchSave(entity0.getClass());
				}else {
					sql = template.batchSave(entity0.getClass());
				}
				
				MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity0, false));
				String preparedSql = mappedStatement.getPreparedSql();
				
				log.debug("sql: " + sql);
				log.debug("preparedSql: " + preparedSql);
				
				pstmt = conn.prepareStatement(preparedSql);
				
				int batchCount = 0;
				for(T entity : entitylist){
					mappedStatement.completePreparedStatement(pstmt, BeanUtil.getFieldsMap(entity, false));
					pstmt.addBatch();
					if(batchCount > 0 && batchCount % batchLimit == 0){
						pstmt.executeBatch();
						conn.commit();
					}
					batchCount++;
				}
				pstmt.executeBatch();
				conn.commit();
				result = entitylist.size();
			}else{
				log.warn("list is null or the list size <= 0");
			}
		} finally {
			conn.setAutoCommit(true);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
		
		return result;
	}

	@Override
	public int delete(long id) throws SQLException {
		String sql = template.delete(beanClass, id);
		
		log.debug("sql: " + sql);
		
		return excute(sql);
	}
	
	@Override
	public int delete(T entity) throws SQLException {
		return delete(entity, null);
	}
	
	@Override
	public int delete(String condition) throws SQLException {
		String sql = template.delete(beanClass, condition);
		
		log.debug("sql: " + sql);
		
		return excute(sql);
	}

	@Override
	public int delete(T entity, String condition) throws SQLException {
		if(null == entity){
			return delete(condition);
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = -1;
		
		try{
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.delete(entity, condition);
			
			MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity, true));
			String preparedSql = mappedStatement.getPreparedSql();
			
			log.debug("sql: " + sql);
			log.debug("preparedSql: " + preparedSql);
			
			pstmt = conn.prepareStatement(preparedSql);
			mappedStatement.completePreparedStatement(pstmt);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			throw new SQLException(e);
		}finally {
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
		return result;
	}
	
	@Override
	public int batchDelete(List<Long> ids)
			throws SQLException {
		String sql = template.batchDelete(beanClass, ids);
		
		log.debug("sql: " + sql);
		
		return excute(sql);
	}

	@Override
	public int update(T entity) throws SQLException {
		return update2(entity, false);
	}

	@Override
	public int enforceUpdate(T entity) throws SQLException {
		return update2(entity, true);
	}
	
	private int update2(T entity, boolean enforce) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = -1;
		
		try{
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = "";
			if(enforce){
				sql = template.enforceUpdate(entity);
			}else{
				sql = template.update(entity);
			}
			
			MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity, !enforce));
			String preparedSql = mappedStatement.getPreparedSql();
			
			log.debug("sql: " + sql);
			log.debug("preparedSql: " + preparedSql);
			
			pstmt = conn.prepareStatement(preparedSql);
			mappedStatement.completePreparedStatement(pstmt);
			
			result = pstmt.executeUpdate();
		}finally {
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
		return result;
	}
	
	@Override
	public int batchUpdate(List<T> entitylist) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = -1;
		
		try{
			conn = ConnectionFactory.getConnection(dsAlias);
			conn.setAutoCommit(false);
			
			if(null != entitylist && entitylist.size() > 0){
				T entity0 = entitylist.get(0);
				String sql = template.batchUpdate(entity0.getClass());
				
				MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity0, false));
				String preparedSql = mappedStatement.getPreparedSql();
				
				log.debug("sql: " + sql);
				log.debug("preparedSql: " + preparedSql);
				
				pstmt = conn.prepareStatement(preparedSql);
				
				int batchCount = 0;
				for(T entity : entitylist){
					mappedStatement.completePreparedStatement(pstmt, BeanUtil.getFieldsMap(entity, false));
					pstmt.addBatch();
					if(batchCount > 0 && batchCount % batchLimit == 0){
						pstmt.executeBatch();
						conn.commit();
					}
					batchCount++;
				}
				pstmt.executeBatch();
				conn.commit();
				result = entitylist.size();
			}else{
				log.warn("list is null or list size < = 0");
			}
		}finally {
			conn.setAutoCommit(true);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
		return result;
	}

	@Override
	public T get(long id, String... fieldNames)
			throws SQLException {
		Connection conn = null;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.get(beanClass, id, fieldNames);
			
			log.debug("sql: " + sql);
			
			return Executor.read(conn, beanClass, sql);
		} finally {
			ConnectionFactory.close(conn);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(T entity, String... fieldNames) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.get(entity, fieldNames);
			
			MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity, true));
			String preparedSql = mappedStatement.getPreparedSql();
			
			log.debug("sql: " + sql);
			log.debug("preparedSql: " + preparedSql);
			
			pstmt = conn.prepareStatement(preparedSql);
			mappedStatement.completePreparedStatement(pstmt);
			
			rs = pstmt.executeQuery();
			
			return Executor.get(rs, (Class<T>)entity.getClass());
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
	}

	@Override
	public List<T> list(List<Long> ids,
			String... fieldNames) throws SQLException {
		Connection conn = null;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.list(beanClass, ids, fieldNames);
			
			log.debug("sql: " + sql);
			
			return Executor.query(conn, beanClass, sql);
		} finally {
			ConnectionFactory.close(conn);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(T entity, Integer page, Integer size,
			String... fieldNames) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.list(entity, page, size, fieldNames);
			
			MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity, true));
			String preparedSql = mappedStatement.getPreparedSql();
			
			log.debug("sql: " + sql);
			log.debug("preparedSql: " + preparedSql);
			
			pstmt = conn.prepareStatement(preparedSql);
			mappedStatement.completePreparedStatement(pstmt);
			
			rs = pstmt.executeQuery();
			
			return Executor.list(rs, (Class<T>)entity.getClass());
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
	}

	@Override
	public List<T> listAll() throws SQLException {
		Connection conn = null;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.listAll(beanClass);
			
			log.debug("sql: " + sql);
			
			return Executor.query(conn, beanClass, sql);
		} finally {
			ConnectionFactory.close(conn);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> filter(T entity, String condition, Integer page,
			Integer size, String orderBy, String... fieldNames) throws SQLException {
		if(null == entity){
			return filter(condition, page, size, orderBy, fieldNames);
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.filter(entity, condition, page, size, orderBy, fieldNames);
			
			MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity, true));
			String preparedSql = mappedStatement.getPreparedSql();
			
			log.debug("sql: " + sql);
			log.debug("preparedSql: " + preparedSql);
			
			pstmt = conn.prepareStatement(preparedSql);
			mappedStatement.completePreparedStatement(pstmt);
			
			rs = pstmt.executeQuery();
			
			return Executor.list(rs, (Class<T>)entity.getClass());
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
	}
	
	@Override
	public List<T> filter(String condition, Integer page,
			Integer size, String orderBy, String... fieldNames) throws SQLException {
		Connection conn = null;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.filter(beanClass, condition, page, size, orderBy, fieldNames);
			
			log.debug("sql: " + sql);
			
			return Executor.query(conn, beanClass, sql);
		} finally {
			ConnectionFactory.close(conn);
		}
	}
	
	@Override
	public long count(T entity) throws SQLException {
		return count(entity, null);
	}
	
	@Override
	public long count(T entity, String condition) throws SQLException {
		if(null == entity){
			return count(condition);
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long result = 0;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.count(entity, condition);
			
			MappedStatement mappedStatement = new MappedStatement(sql, BeanUtil.getFieldsMap(entity, true));
			String preparedSql = mappedStatement.getPreparedSql();
			
			log.debug("sql: " + sql);
			log.debug("preparedSql: " + preparedSql);
			
			pstmt = conn.prepareStatement(preparedSql);
			mappedStatement.completePreparedStatement(pstmt);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				result += rs.getLong(1);
			}
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
		
		return result;
	}
	
	@Override
	public long count(String condition) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long result = 0;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			String sql = template.count(beanClass, condition);
			
			log.debug("sql: " + sql);
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				result += rs.getLong(1);
			}
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
		
		return result;
	}

	@Override
	public int excute(String sql) throws SQLException {
		Connection conn = null;
		
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			log.debug("sql: " + sql);
			
			return Executor.update(conn, sql);
		} finally {
			ConnectionFactory.close(conn);
		}
	}
	
	@Override
	public List<T> query(String sql, Object...params) throws SQLException{
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection(dsAlias);
			
			log.debug("sql: " + sql);
			
			return Executor.query(conn, beanClass, sql, params);
		} finally {
			ConnectionFactory.close(conn);
		}
	}

	public List<List<Object>> executeQuery(String sql) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			log.debug("sql: " + sql);
			conn = ConnectionFactory.getConnection(dsAlias);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			List<List<Object>> list = new ArrayList<List<Object>>();
			int colCount = rs.getMetaData().getColumnCount();
			int i = 1;
			while(rs.next()){
				List<Object> subList = new ArrayList<Object>();
				for(i = 1; i <= colCount; i++){
					subList.add(rs.getObject(i));
				}
				list.add(subList);
			}
			
			return list;
		} finally {
			ConnectionFactory.close(rs);
			ConnectionFactory.close(pstmt);
			ConnectionFactory.close(conn);
		}
	}
	
	
	/**
	 * 样板式代码,不过好像也省不了多少代码,用不用都差不多
	 * @author Administrator
	 *
	 */
	public class DaoTemplate{
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs = null;
		
		public Object sqlHandle() throws SQLException{
			try {
				return customHandle();
			} finally {
				ConnectionFactory.close(rs);
				ConnectionFactory.close(pstmt);
				ConnectionFactory.close(conn);
			}
		}
		
		public Object customHandle() throws SQLException {
			
			return null;
		}
	}
}
