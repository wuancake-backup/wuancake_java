<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%List user = request.getAttribute("user"); 
	if(user == null){
		response.sendRedirect("login.jsp");
	}%>
