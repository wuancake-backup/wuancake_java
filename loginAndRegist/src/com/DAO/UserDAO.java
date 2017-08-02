package com.DAO;


import java.util.List;

import com.Util.DBUtil;
import com.pojo.User;

public class UserDAO {

	
	public List<User> login(String username, String password) {
		String sql = "select username,password from userinfo where username = ? and password = ?";
		
		List<User> list = DBUtil.query(User.class, sql, username,password);
		return list;
	}

	public int regist(String username, String password) {
		
		String sql = "insert into (username,password) values (?,?)";
		int n = DBUtil.zsg(sql, username,password);
		return n;
	}

}
