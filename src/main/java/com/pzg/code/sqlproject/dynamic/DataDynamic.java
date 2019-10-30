package com.pzg.code.sqlproject.dynamic;


import com.pzg.code.sqlproject.utils.ResultToMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：数据库连接
 *
 */
public class DataDynamic {

	public static Statement st;
	public static Connection conn;

	public void getConnection() throws Exception {
	}

	public List<Map> selectTableData(String tableName, Integer pageNum, Integer pageSize) throws Exception {
		return null;
	}

	public List<Map> selectTableInfo(String tableName) throws Exception {
		return null;
	}

	public List<Map> selectTableDataCount(String tableName) throws Exception {
		return null;
	}

	/**
	 * sql执行
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map> executeSql(String sql) {
		List records = new ArrayList();
		try {
			// 创建用于执行静态sql语句的Statement对象，st属局部变量
			st = (Statement) conn.createStatement();
			// 执行sql查询语句，返回查询数据的结果集
			ResultSet rs = st.executeQuery(sql);
			records = cast(rs);
			// 关闭数据库连接
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("查询数据失败");
		}
		return records;
	}

	/**
	 * 转换结果到list
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public List<Map> cast(ResultSet rs) throws Exception {
		List<Map> list = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int fieldCount = rsmd.getColumnCount();
		while (rs.next()) {
			Map<String, String> valueMap = new LinkedHashMap<String, String>();
			for (int i = 1; i <= fieldCount; i++) {
				String fieldClassName = rsmd.getColumnClassName(i);
				String fieldName = rsmd.getColumnLabel(i);
				new ResultToMap()._recordMappingToMap(fieldClassName, fieldName, rs, valueMap);
			}
			list.add(valueMap);
		}
		return list;
	}
}
