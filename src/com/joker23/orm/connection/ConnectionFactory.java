package com.joker23.orm.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 连接池初始化
 * @author Joker
 *
 */
public class ConnectionFactory {
	
	private static Map<String, DataSource> dsMap;

	private ConnectionFactory() {
		
	}
	
	/**
	 * 初始化数据库池(使用druid)
	 * @param alias	数据库别名
	 * @param configName	连接池配置文件名 路径(classes/configName)
	 * @throws Exception
	 */
	public static void init(String alias, String configName) throws Exception{
		InputStream in = null;
		Properties prop = new Properties();
		in = ConnectionFactory.class.getResourceAsStream("/" + configName);
		prop.load(in);
		build(alias, (DruidDataSource) DruidDataSourceFactory.createDataSource(prop));
		if(null != in) {
			in.close();
			in = null;
		}
	}
	
	/**
	 * 配置数据库别名
	 * @param alias
	 * @param ds
	 */
	public static void build(String alias, DataSource ds) {
		if(null == ConnectionFactory.dsMap) {
			ConnectionFactory.dsMap = new ConcurrentHashMap<String, DataSource>();
		}
		ConnectionFactory.dsMap.put(alias, ds);
	}
	
	/**
	 * 获取数据库连接
	 * @param alias	数据库池初始化时的别名
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String alias) throws SQLException {
		DataSource ds = dsMap.get(alias);
		if(null == ds) {
			throw new SQLException("Datasource is not initialized...");
		}
		return ds.getConnection();
	}
	
	public static void close(Connection conn) {
		try {
			if(null != conn && !conn.isClosed()) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(null != pstmt && !pstmt.isClosed()) {
				pstmt.close();
				pstmt = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt) {
		try {
			if(null != stmt && !stmt.isClosed()) {
				stmt.close();
				stmt = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			if(null != rs && !rs.isClosed()) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
