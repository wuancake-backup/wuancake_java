package com.servlet;

import java.io.IOException;
import java.util.List;

import javax.jms.Session;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.UserException;

import com.DAO.UserDAO;
import com.pojo.User;

public class UserServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String method = request.getParameter("method");
		
		if ("regist".equals(method)) {
			 doRegist(request,response);
		}
		if ("login".equals(method)) {
			doLogin(request,response);
		}
		
	}

	private void doLogin(HttpServletRequest request, HttpServletResponse response) {
		String usernama = request.getParameter("name");
		String password = request.getParameter("password");
		UserDAO dao = new UserDAO();
		List<User> list = dao.login(usernama,password);
		if (list != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", list);
			System.out.println("登陆成功");
			try {
				request.getRequestDispatcher("index.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void doRegist(HttpServletRequest request, HttpServletResponse response) {

		String usernama = request.getParameter("name");
		String password = request.getParameter("password");
		UserDAO dao = new UserDAO();
		int n = dao.regist(usernama,password);
		if (n > 0) {
			System.out.println("注册成功");
			try {
				response.sendRedirect("login.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
}
