package com.Util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DBUtil {

	//实例化连接池
	public static Vector<Connection> connectionPool = new Vector<Connection>();

	// 初始化连接池
	static {

		for (int i = 0; i < 10; i++) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(
						"jdbc:mysql://127.0.0.1:3306/user", "root", "admin");

				connectionPool.add(connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 取链接
	public static Connection getConnection() {

		Connection connection = connectionPool.get(0);
		connectionPool.remove(0);
		return connection;

	}

	//连接放回连接池，释放链接
	public static void releaseConnection(Connection connection) {
		connectionPool.add(connection);
	}

	//增删改
	public static int zsg(String sql, Object... p) {
		Connection connection = getConnection();

		int n = 0;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);

			for (int i = 0; i < p.length; i++) {
				ps.setObject(i + 1, p[i]);
			}

			n = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(connection);
		}

		return n;

	}

	//查询

	public static List query(Class c, String sql, Object... p) {

		List list = new ArrayList();

		Connection connection = getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(sql);

			for (int i = 0; i < p.length; i++) {
				ps.setObject(i + 1, p[i]);
			}

			ResultSet rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			// 总列数
			int count = rsmd.getColumnCount();

			while (rs.next()) {
				Object object = c.newInstance();
				for (int i = 1; i <= count; i++) {
					String fieldname = rsmd.getColumnLabel(i); //得到数据库的字段名
					Field field = c.getDeclaredField(fieldname);

					// 给私有属性赋值
					field.setAccessible(true);
					field.set(object, rs.getObject(i));

				}

				list.add(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			releaseConnection(connection);
		}

		return list;

	}

	//聚集查询 
	public static int uniqueQuery(String sql, Object... p) {

		int count = 0;

		Connection connection = getConnection();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);

			for (int i = 0; i < p.length; i++) {
				ps.setObject(i + 1, p[i]);
			}

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(connection);
		}

		return count;

	}

}
